package com.maks.wealthos.transaction.dto;

import com.maks.wealthos.common.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Request body for creating a new transaction")
public class CreateTransactionRequest {

    @NotNull
    @Schema(description = "User ID associated with the transaction", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "Category ID for the transaction", example = "1")
    private Long categoryId;

    @NotNull
    @Schema(description = "Date of the transaction", example = "2025-01-15")
    private LocalDate transactionDate;

    @NotNull
    @Schema(description = "Type of transaction", example = "INCOME",
            allowableValues = {"INCOME", "EXPENSE", "TRANSFER", "ADJUSTMENT"})
    private TransactionType transactionType;

    @NotNull
    @Positive
    @Schema(description = "Transaction amount (must be positive)", example = "1500.00")
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