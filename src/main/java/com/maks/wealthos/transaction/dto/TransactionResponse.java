package com.maks.wealthos.transaction.dto;

import com.maks.wealthos.common.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {

    private Long transactionId;

    private Long userId;

    private Long categoryId;

    private LocalDate transactionDate;

    private TransactionType transactionType;

    private BigDecimal amount;

    private String description;

    private String accountName;

    private String referenceNumber;

    private String remarks;
}