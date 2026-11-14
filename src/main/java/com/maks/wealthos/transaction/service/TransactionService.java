package com.maks.wealthos.transaction.service;

import com.maks.wealthos.common.enums.TransactionType;
import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;
import com.maks.wealthos.transaction.dto.UpdateTransactionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    // ========================================================================
    //  Core CRUD operations
    // ========================================================================

    /**
     * Create a new transaction
     */
    TransactionResponse createTransaction(
            CreateTransactionRequest request);

    /**
     * Get a transaction by ID
     */
    TransactionResponse getTransaction(
            Long transactionId);

    /**
     * Update an existing transaction
     */
    TransactionResponse updateTransaction(
            Long transactionId,
            UpdateTransactionRequest request);

    /**
     * Delete a transaction by ID
     */
    void deleteTransaction(
            Long transactionId);

    // ========================================================================
    //  List operations
    // ========================================================================

    /**
     * Get all transactions (paginated)
     */
    Page<TransactionResponse> getAllTransactions(Pageable pageable);

    /**
     * Get all transactions (non-paginated, for backward compatibility)
     */
    List<TransactionResponse> getAllTransactions();

    // ========================================================================
    //  User-based queries
    // ========================================================================

    /**
     * Get transactions by user ID
     */
    List<TransactionResponse> getTransactionsByUser(
            Long userId);

    /**
     * Get transactions by user ID (paginated)
     */
    Page<TransactionResponse> getTransactionsByUser(
            Long userId, Pageable pageable);

    // ========================================================================
    //  Type-based queries
    // ========================================================================

    /**
     * Get transactions by transaction type
     */
    List<TransactionResponse> getTransactionsByTransactionType(
            TransactionType transactionType);

    /**
     * Get user transactions by type
     */
    List<TransactionResponse> getTransactionsByUserAndType(
            Long userId,
            TransactionType transactionType);

    // ========================================================================
    //  Date range queries
    // ========================================================================

    /**
     * Get transactions by date range
     */
    List<TransactionResponse> getTransactionsByDateRange(
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Get user transactions by date range
     */
    List<TransactionResponse> getTransactionsByUserAndDateRange(
            Long userId,
            LocalDate startDate,
            LocalDate endDate);

    // ========================================================================
    //  Recent transactions
    // ========================================================================

    /**
     * Get recent transactions for a user
     */
    List<TransactionResponse> getRecentTransactions(
            Long userId,
            int limit);

    /**
     * Get recent transactions for a user (paginated)
     */
    Page<TransactionResponse> getRecentTransactions(
            Long userId,
            Pageable pageable);
}