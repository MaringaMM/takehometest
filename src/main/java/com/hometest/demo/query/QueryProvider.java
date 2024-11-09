package com.hometest.demo.query;

/**
 * Provides reusable SQL queries for the application.
 * This class centralizes SQL query management to promote reuse and maintainability.
 */
public class QueryProvider {

    private QueryProvider() {
        // Private constructor to prevent instantiation
    }

    // SQL Queries for account operations
    public static final String SELECT_ACCOUNT_BALANCE = "SELECT balance FROM accounts WHERE id = ?";
    public static final String UPDATE_ACCOUNT_BALANCE = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
}

