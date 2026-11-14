package com.maks.wealthos.transaction.controller;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.common.enums.TransactionType;
import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;
import com.maks.wealthos.transaction.dto.UpdateTransactionRequest;
import com.maks.wealthos.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management endpoints")
public class TransactionController {

    private final TransactionService service;

    // ========================================================================
    //  Core CRUD operations
    // ========================================================================

    @PostMapping
    @Operation(
            summary = "Create a new transaction",
            description = "Creates a new transaction record with the provided details."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<TransactionResponse>> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        log.info(
                "{} Create request received. Type={}, Amount={}, CategoryId={}",
                LogConstants.TRANSACTION_MODULE,
                request.getTransactionType(),
                request.getAmount(),
                request.getCategoryId()
        );

        TransactionResponse response =
                service.createTransaction(request);

        log.info(
                "{} Transaction created successfully. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                response.getTransactionId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new com.maks.wealthos.common.response.ApiResponse<>(
                        "Transaction created successfully",
                        response
                ));
    }

    @GetMapping("/{transactionId}")
    @Operation(
            summary = "Get transaction by ID",
            description = "Retrieves a single transaction by its unique identifier."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transaction found",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<TransactionResponse>> getTransaction(
            @Parameter(description = "Transaction ID", example = "1")
            @PathVariable Long transactionId) {

        log.info(
                "{} Fetching transaction. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        TransactionResponse response =
                service.getTransaction(transactionId);

        log.info(
                "{} Transaction fetched successfully. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Transaction fetched successfully",
                response
        ));
    }

    @PutMapping("/{transactionId}")
    @Operation(
            summary = "Update a transaction",
            description = "Updates an existing transaction with the provided details."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<TransactionResponse>> updateTransaction(
            @Parameter(description = "Transaction ID", example = "1")
            @PathVariable Long transactionId,
            @Valid @RequestBody UpdateTransactionRequest request) {

        log.info(
                "{} Update request received. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        TransactionResponse response =
                service.updateTransaction(transactionId, request);

        log.info(
                "{} Transaction updated successfully. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Transaction updated successfully",
                response
        ));
    }

    @DeleteMapping("/{transactionId}")
    @Operation(
            summary = "Delete a transaction",
            description = "Deletes a transaction by its unique identifier."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transaction deleted successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<String>> deleteTransaction(
            @Parameter(description = "Transaction ID", example = "1")
            @PathVariable Long transactionId) {

        log.info(
                "{} Delete request received. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        service.deleteTransaction(transactionId);

        log.info(
                "{} Transaction deleted successfully. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Transaction deleted successfully",
                "SUCCESS"
        ));
    }

    // ========================================================================
    //  List operations
    // ========================================================================

    @GetMapping
    @Operation(
            summary = "Get all transactions (paginated)",
            description = "Retrieves all transactions with pagination support."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<Page<TransactionResponse>>> getAllTransactions(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "transactionDate")
            @RequestParam(defaultValue = "transactionDate") String sort,
            @Parameter(description = "Sort direction (asc/desc)", example = "desc")
            @RequestParam(defaultValue = "desc") String direction) {

        log.info(
                "{} Fetching all transactions with pagination. Page={}, Size={}, Sort={}, Direction={}",
                LogConstants.TRANSACTION_MODULE,
                page, size, sort, direction
        );

        Sort sortObj =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.by(sort).descending()
                        : Sort.by(sort).ascending();

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<TransactionResponse> transactions =
                service.getAllTransactions(pageable);

        log.info(
                "{} Total transaction pages={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.getTotalPages()
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Transactions fetched successfully",
                transactions
        ));
    }

    // ========================================================================
    //  User-based queries
    // ========================================================================

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get transactions by user ID",
            description = "Retrieves all transactions for a specific user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<List<TransactionResponse>>> getTransactionsByUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId) {

        log.info(
                "{} Fetching transactions for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                userId
        );

        List<TransactionResponse> transactions =
                service.getTransactionsByUser(userId);

        log.info(
                "{} Total transactions fetched={} for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                userId
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Transactions fetched successfully",
                transactions
        ));
    }

    // ========================================================================
    //  Type-based queries
    // ========================================================================

    @GetMapping("/type/{transactionType}")
    @Operation(
            summary = "Get transactions by type",
            description = "Retrieves all transactions filtered by transaction type (INCOME, EXPENSE, TRANSFER, ADJUSTMENT)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid transaction type",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<List<TransactionResponse>>> getTransactionsByType(
            @Parameter(description = "Transaction type", example = "INCOME",
                    schema = @Schema(allowableValues = {"INCOME", "EXPENSE", "TRANSFER", "ADJUSTMENT"}))
            @PathVariable TransactionType transactionType) {

        log.info(
                "{} Fetching transactions by type={}",
                LogConstants.TRANSACTION_MODULE,
                transactionType
        );

        List<TransactionResponse> transactions =
                service.getTransactionsByTransactionType(transactionType);

        log.info(
                "{} Total transactions fetched={} for type={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                transactionType
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Transactions fetched successfully",
                transactions
        ));
    }

    // ========================================================================
    //  Date range queries
    // ========================================================================

    @GetMapping("/date-range")
    @Operation(
            summary = "Get transactions by date range",
            description = "Retrieves transactions within a specific date range."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid date range",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<List<TransactionResponse>>> getTransactionsByDateRange(
            @Parameter(description = "Start date (inclusive)", example = "2025-01-01")
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (inclusive)", example = "2025-12-31")
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info(
                "{} Fetching transactions between {} and {}",
                LogConstants.TRANSACTION_MODULE,
                startDate,
                endDate
        );

        List<TransactionResponse> transactions =
                service.getTransactionsByDateRange(startDate, endDate);

        log.info(
                "{} Total transactions fetched={} between {} and {}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                startDate,
                endDate
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Transactions fetched successfully",
                transactions
        ));
    }

    // ========================================================================
    //  Recent transactions
    // ========================================================================

    @GetMapping("/recent/{userId}")
    @Operation(
            summary = "Get recent transactions for a user",
            description = "Retrieves the most recent transactions for a specific user."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Recent transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.maks.wealthos.common.response.ApiResponse.class))
            )
    })
    public ResponseEntity<com.maks.wealthos.common.response.ApiResponse<List<TransactionResponse>>> getRecentTransactions(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Number of recent transactions to fetch", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        log.info(
                "{} Fetching recent {} transactions for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                limit,
                userId
        );

        List<TransactionResponse> transactions =
                service.getRecentTransactions(userId, limit);

        log.info(
                "{} Recent transactions fetched={} for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                userId
        );

        return ResponseEntity.ok(new com.maks.wealthos.common.response.ApiResponse<>(
                "Recent transactions fetched successfully",
                transactions
        ));
    }
}