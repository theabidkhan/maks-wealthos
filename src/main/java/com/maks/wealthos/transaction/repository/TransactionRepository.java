package com.maks.wealthos.transaction.repository;

import com.maks.wealthos.common.enums.TransactionType;
import com.maks.wealthos.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * Fetch all transactions of a user
     */
    List<TransactionEntity> findByUserId(Long userId);

    /**
     * Fetch all transactions of a category
     */
    List<TransactionEntity> findByCategoryId(Long categoryId);

    /**
     * Fetch all transactions by type
     */
    List<TransactionEntity> findByTransactionType(
            TransactionType transactionType);

    /**
     * Fetch user transactions by type
     */
    List<TransactionEntity> findByUserIdAndTransactionType(
            Long userId,
            TransactionType transactionType);

    /**
     * Fetch user transactions in date range
     */
    List<TransactionEntity> findByUserIdAndTransactionDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Latest transactions of user
     */
    List<TransactionEntity> findTop10ByUserIdOrderByTransactionDateDesc(
            Long userId);

    /**
     * Validate duplicate reference number
     */
    boolean existsByReferenceNumber(
            String referenceNumber);

    /**
     * Total transaction amount for user
     */
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.userId = :userId
            """)
    BigDecimal getTotalTransactionAmount(
            @Param("userId") Long userId);

    /**
     * Total income/expense amount by type
     */
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.userId = :userId
            AND t.transactionType = :transactionType
            """)
    BigDecimal getTotalAmountByTransactionType(
            @Param("userId") Long userId,
            @Param("transactionType") TransactionType transactionType);

    /**
     * Monthly/Custom period total
     */
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.userId = :userId
            AND t.transactionDate BETWEEN :startDate AND :endDate
            """)
    BigDecimal getTotalAmountBetweenDates(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Transaction count for dashboard
     */
    long countByUserId(Long userId);

    /**
     * Current month transactions
     */
    List<TransactionEntity> findByUserIdAndTransactionDateGreaterThanEqual(
            Long userId,
            LocalDate startDate);

    /**
     * Search by reference number
     */
    TransactionEntity findByReferenceNumber(
            String referenceNumber);
}