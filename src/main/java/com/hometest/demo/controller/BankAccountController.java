package com.hometest.demo.controller;

import com.hometest.demo.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

import static com.hometest.demo.constants.MessagesConstants.WITHDRAWAL_FAILED_MESSAGE;

/**
 * Controller for handling bank account-related operations, such as withdrawals.
 * Versioned under /api/v1/bank to support future versioning of the API.
 */
@RestController
@RequestMapping("/api/v1/bank")
@Slf4j
@RequiredArgsConstructor
public class BankAccountController {

    private final BankService bankService;

    /**
     * Initiates a withdrawal from the specified bank account.
     *
     * @param accountId The ID of the bank account.
     * @param amount    The amount to withdraw.
     * @return A message indicating the result of the withdrawal operation.
     */
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountId") Long accountId,
                           @RequestParam("amount") BigDecimal amount) {
        try {
            log.info("Initiating withdrawal for accountId: {} with amount: {}", accountId, amount);
            return bankService.processWithdrawal(accountId, amount);
        } catch (Exception e) {
            log.error("Withdrawal failed for accountId: {}", accountId, e);
            return WITHDRAWAL_FAILED_MESSAGE;
        }
    }
}

