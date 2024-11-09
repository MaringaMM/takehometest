package com.hometest.demo.service;

import com.hometest.demo.dto.WithdrawalEvent;
import com.hometest.demo.exception.InsufficientFundsException;
import com.hometest.demo.repository.BankAccountRepository;
import config.AwsConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.math.BigDecimal;

import static com.hometest.demo.constants.EventConstants.WITHDRAWAL_SUCCESS_STATUS;
import static com.hometest.demo.constants.MessagesConstants.*;

/**
 * Service for handling business logic related to bank account operations, such as processing withdrawals.
 * Responsible for account balance checks, withdrawal execution, and publishing withdrawal events.
 */
@Service
@Slf4j
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final SnsClient snsClient;
    private final AwsConfigProperties awsConfigProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public BankService(BankAccountRepository bankAccountRepository, SnsClient snsClient, AwsConfigProperties awsConfigProperties) {
        this.bankAccountRepository = bankAccountRepository;
        this.snsClient = snsClient;
        this.awsConfigProperties = awsConfigProperties;
    }

    /**
     * Processes a withdrawal by checking balance, updating the account, and sending a notification.
     *
     * @param accountId The ID of the bank account.
     * @param amount    The amount to withdraw.
     * @return A message indicating the result of the withdrawal operation.
     * @throws InsufficientFundsException if the account balance is insufficient.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public String processWithdrawal(Long accountId, BigDecimal amount) {
        BigDecimal currentBalance = bankAccountRepository.getBalance(accountId);

        if (currentBalance != null && currentBalance.compareTo(amount) >= 0) {
            int rowsAffected = bankAccountRepository.updateBalance(accountId, amount);

            if (rowsAffected > 0) {
                sendWithdrawalEventAsync(accountId, amount);
                log.info("Withdrawal successful for accountId: {} with amount: {}", accountId, amount);
                return WITHDRAWAL_SUCCESS_MESSAGE;
            } else {
                log.warn("Withdrawal failed due to unexpected database update issue for accountId: {}", accountId);
                return WITHDRAWAL_FAILED_MESSAGE;
            }
        } else {
            log.info("Insufficient funds for accountId: {}", accountId);
            throw new InsufficientFundsException(INSUFFICIENT_FUNDS_MESSAGE);
        }
    }

    /**
     * Asynchronously publishes a withdrawal event to SNS to notify other systems of the transaction.
     * Upon failure, retries the operation with a delay.
     * @param accountId The ID of the bank account.
     * @param amount    The amount withdrawn.
     */
    @Async
    @Retryable(value = SnsException.class, backoff = @Backoff(delay = 2000))
    protected void sendWithdrawalEventAsync(Long accountId, BigDecimal amount) {
        try {
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, WITHDRAWAL_SUCCESS_STATUS);
            String eventJson = objectMapper.writeValueAsString(event);

            PublishRequest publishRequest = PublishRequest.builder()
                    .message(eventJson)
                    .topicArn(awsConfigProperties.getTopicArn())
                    .build();
            snsClient.publish(publishRequest);
            log.info("Withdrawal event published for accountId: {} with status: {}", accountId, WITHDRAWAL_SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("Failed to publish withdrawal event for accountId: {}", accountId, e);
        }
    }
}

