package com.maks.wealthos.transaction.dto;

import com.maks.wealthos.common.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTransactionRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long categoryId;

    @NotNull
    private LocalDate transactionDate;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;

    private String accountName;

    private String referenceNumber;

    private String remarks;
}