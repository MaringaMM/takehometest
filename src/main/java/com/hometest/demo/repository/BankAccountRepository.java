package com.hometest.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

import static com.hometest.demo.query.QueryProvider.SELECT_ACCOUNT_BALANCE;
import static com.hometest.demo.query.QueryProvider.UPDATE_ACCOUNT_BALANCE;

/**
 * Repository for accessing and updating bank account information in the database.
 * Encapsulates database queries related to account balances.
 */
@Repository
public class BankAccountRepository {

    public static final String ACCOUNT_ID_KEY = "accountId";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Constructs a BankAccountRepository with the specified JdbcTemplate.
     *
     * @param jdbcTemplate The JdbcTemplate for database interactions.
     */
    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves the current balance of a specified account.
     *
     * @param accountId The ID of the account.
     * @return The current balance as a BigDecimal.
     */
    @Cacheable(value = "accountBalances", key = "#accountId")
    public BigDecimal getBalance(Long accountId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(ACCOUNT_ID_KEY, accountId);

        return namedParameterJdbcTemplate.queryForObject(
                SELECT_ACCOUNT_BALANCE,
                params,
                BigDecimal.class
        );
    }

    /**
     * Updates the balance of a specified account by subtracting the specified amount.
     *
     * @param accountId The ID of the account.
     * @param amount    The amount to deduct from the balance.
     * @return The number of rows affected.
     */
    public int updateBalance(Long accountId, BigDecimal amount) {
        return jdbcTemplate.update(UPDATE_ACCOUNT_BALANCE, amount, accountId);
    }
}

