package com.maks.wealthos.transaction.service;

import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(
            CreateTransactionRequest request);

    TransactionResponse getTransaction(
            Long transactionId);

    List<TransactionResponse> getAllTransactions();

    List<TransactionResponse> getTransactionsByUser(
            Long userId);

    void deleteTransaction(
            Long transactionId);

    TransactionResponse updateTransaction(
            Long transactionId,
            CreateTransactionRequest request);
}