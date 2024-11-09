package com.hometest.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WithdrawalEvent {
    private BigDecimal amount;
    private Long accountId;
    private String status;
}
