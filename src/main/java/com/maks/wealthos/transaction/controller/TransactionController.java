package com.maks.wealthos.transaction.controller;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.common.response.ApiResponse;
import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;
import com.maks.wealthos.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ApiResponse<TransactionResponse> createTransaction(
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

        return new ApiResponse<>(
                "Transaction created successfully",
                response
        );
    }

    @GetMapping("/{transactionId}")
    public ApiResponse<TransactionResponse> getTransaction(
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

        return new ApiResponse<>(
                "Transaction fetched successfully",
                response
        );
    }

    @GetMapping
    public ApiResponse<List<TransactionResponse>> getAllTransactions() {

        log.info("{} Fetching all transactions",LogConstants.TRANSACTION_MODULE);

        List<TransactionResponse> transactions =
                service.getAllTransactions();

        log.info(
                "{} Total transactions fetched={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size()
        );

        return new ApiResponse<>(
                "Transactions fetched successfully",
                transactions
        );
    }
}