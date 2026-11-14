package com.maks.wealthos.transaction.dto;

import com.maks.wealthos.common.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Transaction response object")
public class TransactionResponse {

    @Schema(description = "Unique transaction identifier", example = "1")
    private Long transactionId;

    @Schema(description = "User ID associated with the transaction", example = "1")
    private Long userId;

    @Schema(description = "Category ID for the transaction", example = "1")
    private Long categoryId;

    @Schema(description = "Date of the transaction", example = "2025-01-15")
    private LocalDate transactionDate;

    @Schema(description = "Type of transaction", example = "INCOME")
    private TransactionType transactionType;

    @Schema(description = "Transaction amount", example = "1500.00")
    private BigDecimal amount;

    @Schema(description = "Short description of the transaction", example = "Monthly salary")
    private String description;

    @Schema(description = "Account name for the transaction", example = "Main Savings")
    private String accountName;

    @Schema(description = "Reference number for tracking", example = "REF-2025-001")
    private String referenceNumber;

    @Schema(description = "Additional remarks or notes", example = "January salary payment")
    private String remarks;
}