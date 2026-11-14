package com.maks.wealthos.transaction.repository;

import com.maks.wealthos.common.enums.TransactionType;
import com.maks.wealthos.transaction.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // ========================================================================
    //  User-based searches
    // ========================================================================

    /**
     * Fetch all transactions of a user
     */
    List<TransactionEntity> findByUserId(Long userId);

    /**
     * Fetch all transactions of a user with pagination
     */
    Page<TransactionEntity> findByUserId(Long userId, Pageable pageable);

    // ========================================================================
    //  Category-based searches
    // ========================================================================

    /**
     * Fetch all transactions of a category
     */
    List<TransactionEntity> findByCategoryId(Long categoryId);

    // ========================================================================
    //  Type-based searches
    // ========================================================================

    /**
     * Fetch all transactions by type
     */
    List<TransactionEntity> findByTransactionType(
            TransactionType transactionType);

    /**
     * Fetch all transactions by type with pagination
     */
    Page<TransactionEntity> findByTransactionType(
            TransactionType transactionType, Pageable pageable);

    /**
     * Fetch user transactions by type
     */
    List<TransactionEntity> findByUserIdAndTransactionType(
            Long userId,
            TransactionType transactionType);

    // ========================================================================
    //  Date range searches
    // ========================================================================

    /**
     * Fetch user transactions in date range
     */
    List<TransactionEntity> findByUserIdAndTransactionDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Fetch transactions in date range (all users)
     */
    List<TransactionEntity> findByTransactionDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Fetch transactions in date range with pagination
     */
    @Query("""
            SELECT t FROM TransactionEntity t
            WHERE t.transactionDate BETWEEN :startDate AND :endDate
            ORDER BY t.transactionDate DESC
            """)
    Page<TransactionEntity> findByTransactionDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    // ========================================================================
    //  Recent transactions
    // ========================================================================

    /**
     * Latest transactions of user (top 10)
     */
    List<TransactionEntity> findTop10ByUserIdOrderByTransactionDateDesc(
            Long userId);

    /**
     * Latest N transactions of a user
     */
    @Query("""
            SELECT t FROM TransactionEntity t
            WHERE t.userId = :userId
            ORDER BY t.transactionDate DESC, t.transactionId DESC
            """)
    Page<TransactionEntity> findRecentByUserId(
            @Param("userId") Long userId,
            Pageable pageable);

    // ========================================================================
    //  Reference number checks
    // ========================================================================

    /**
     * Validate duplicate reference number
     */
    boolean existsByReferenceNumber(
            String referenceNumber);

    /**
     * Search by reference number
     */
    TransactionEntity findByReferenceNumber(
            String referenceNumber);

    // ========================================================================
    //  Dashboard aggregation queries
    // ========================================================================

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
     * Total income for user
     */
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.userId = :userId
            AND t.transactionType = :transactionType
            """)
    BigDecimal getTotalIncome(
            @Param("userId") Long userId);

    /**
     * Total expense for user
     */
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.userId = :userId
            AND t.transactionType = :transactionType
            """)
    BigDecimal getTotalExpense(
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
     * Monthly transaction total for a user
     */
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.userId = :userId
            AND t.transactionDate BETWEEN :startDate AND :endDate
            AND t.transactionType = :transactionType
            """)
    BigDecimal getMonthlyTotal(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("transactionType") TransactionType transactionType);

    /**
     * Transaction count for dashboard
     */
    long countByUserId(Long userId);

    /**
     * Transaction count by type for a user
     */
    long countByUserIdAndTransactionType(
            Long userId,
            TransactionType transactionType);

    /**
     * User transaction summary: count and total by type
     */
    @Query("""
            SELECT t.transactionType, COUNT(t), COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.userId = :userId
            GROUP BY t.transactionType
            """)
    List<Object[]> getUserTransactionSummary(@Param("userId") Long userId);

    // ========================================================================
    //  Period-based queries
    // ========================================================================

    /**
     * Current month transactions
     */
    List<TransactionEntity> findByUserIdAndTransactionDateGreaterThanEqual(
            Long userId,
            LocalDate startDate);
}