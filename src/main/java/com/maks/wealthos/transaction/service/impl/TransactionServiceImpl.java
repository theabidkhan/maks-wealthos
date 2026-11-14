package com.maks.wealthos.transaction.service.impl;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.common.enums.TransactionType;
import com.maks.wealthos.common.exception.ResourceNotFoundException;
import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;
import com.maks.wealthos.transaction.dto.UpdateTransactionRequest;
import com.maks.wealthos.transaction.entity.TransactionEntity;
import com.maks.wealthos.transaction.mapper.TransactionMapper;
import com.maks.wealthos.transaction.repository.TransactionRepository;
import com.maks.wealthos.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    // ========================================================================
    //  Core CRUD operations
    // ========================================================================

    @Override
    public TransactionResponse createTransaction(
            CreateTransactionRequest request) {

        log.info(
                "{} Creating transaction. Type={}, Amount={}, UserId={}, CategoryId={}",
                LogConstants.TRANSACTION_MODULE,
                request.getTransactionType(),
                request.getAmount(),
                request.getUserId(),
                request.getCategoryId()
        );

        TransactionEntity entity = mapper.toEntity(request);

        TransactionEntity saved = repository.save(entity);

        log.info(
                "{} Transaction persisted successfully. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                saved.getTransactionId()
        );

        return mapper.toResponse(saved);
    }

    @Override
    public TransactionResponse getTransaction(
            Long transactionId) {

        log.info(
                "{} Fetching transaction. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        TransactionEntity transaction =
                repository.findById(transactionId)
                        .orElseThrow(() -> {

                            log.error(
                                    "{} Transaction not found. TransactionId={}",
                                    LogConstants.TRANSACTION_MODULE,
                                    transactionId
                            );

                            return new ResourceNotFoundException(
                                    "Transaction not found : "
                                            + transactionId
                            );
                        });

        log.info(
                "{} Transaction found. TransactionId={}, Amount={}",
                LogConstants.TRANSACTION_MODULE,
                transaction.getTransactionId(),
                transaction.getAmount()
        );

        return mapper.toResponse(transaction);
    }

    @Override
    public TransactionResponse updateTransaction(
            Long transactionId,
            UpdateTransactionRequest request) {

        log.info(
                "{} Updating transaction. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        TransactionEntity existing =
                repository.findById(transactionId)
                        .orElseThrow(() -> {

                            log.error(
                                    "{} Transaction not found. TransactionId={}",
                                    LogConstants.TRANSACTION_MODULE,
                                    transactionId
                            );

                            return new ResourceNotFoundException(
                                    "Transaction not found : " + transactionId
                            );
                        });

        existing.setUserId(request.getUserId());
        existing.setCategoryId(request.getCategoryId());
        existing.setTransactionDate(request.getTransactionDate());
        existing.setTransactionType(request.getTransactionType());
        existing.setAmount(request.getAmount());
        existing.setDescription(request.getDescription());
        existing.setAccountName(request.getAccountName());
        existing.setReferenceNumber(request.getReferenceNumber());
        existing.setRemarks(request.getRemarks());

        TransactionEntity updated =
                repository.save(existing);

        log.info(
                "{} Transaction updated successfully. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                updated.getTransactionId()
        );

        return mapper.toResponse(updated);
    }

    @Override
    public void deleteTransaction(Long transactionId) {

        log.info(
                "{} Deleting transaction. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );

        TransactionEntity transaction =
                repository.findById(transactionId)
                        .orElseThrow(() -> {

                            log.error(
                                    "{} Transaction not found. TransactionId={}",
                                    LogConstants.TRANSACTION_MODULE,
                                    transactionId
                            );

                            return new ResourceNotFoundException(
                                    "Transaction not found : " + transactionId
                            );
                        });

        repository.delete(transaction);

        log.info(
                "{} Transaction deleted successfully. TransactionId={}",
                LogConstants.TRANSACTION_MODULE,
                transactionId
        );
    }

    // ========================================================================
    //  List operations
    // ========================================================================

    @Override
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {

        log.info(
                "{} Fetching all transactions with pagination. Page={}, Size={}",
                LogConstants.TRANSACTION_MODULE,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<TransactionResponse> transactions =
                repository.findAll(pageable)
                        .map(mapper::toResponse);

        log.info(
                "{} Total transaction pages={}, Current page size={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.getTotalPages(),
                transactions.getContent().size()
        );

        return transactions;
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {

        log.info("{} Fetching all transactions", LogConstants.TRANSACTION_MODULE);

        List<TransactionResponse> transactions =
                repository.findAll()
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        log.info(
                "{} Total transactions fetched={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size()
        );

        return transactions;
    }

    // ========================================================================
    //  User-based queries
    // ========================================================================

    @Override
    public List<TransactionResponse> getTransactionsByUser(Long userId) {

        log.info(
                "{} Fetching transactions for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                userId
        );

        List<TransactionResponse> transactions =
                repository.findByUserId(userId)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        log.info(
                "{} Total transactions fetched={} for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                userId
        );

        return transactions;
    }

    @Override
    public Page<TransactionResponse> getTransactionsByUser(
            Long userId, Pageable pageable) {

        log.info(
                "{} Fetching transactions for UserId={} with pagination. Page={}, Size={}",
                LogConstants.TRANSACTION_MODULE,
                userId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<TransactionResponse> transactions =
                repository.findByUserId(userId, pageable)
                        .map(mapper::toResponse);

        log.info(
                "{} Total transaction pages={} for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.getTotalPages(),
                userId
        );

        return transactions;
    }

    // ========================================================================
    //  Type-based queries
    // ========================================================================

    @Override
    public List<TransactionResponse> getTransactionsByTransactionType(
            TransactionType transactionType) {

        log.info(
                "{} Fetching transactions by type={}",
                LogConstants.TRANSACTION_MODULE,
                transactionType
        );

        List<TransactionResponse> transactions =
                repository.findByTransactionType(transactionType)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        log.info(
                "{} Total transactions fetched={} for type={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                transactionType
        );

        return transactions;
    }

    @Override
    public List<TransactionResponse> getTransactionsByUserAndType(
            Long userId,
            TransactionType transactionType) {

        log.info(
                "{} Fetching transactions for UserId={} and type={}",
                LogConstants.TRANSACTION_MODULE,
                userId,
                transactionType
        );

        List<TransactionResponse> transactions =
                repository.findByUserIdAndTransactionType(userId, transactionType)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        log.info(
                "{} Total transactions fetched={} for UserId={} and type={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                userId,
                transactionType
        );

        return transactions;
    }

    // ========================================================================
    //  Date range queries
    // ========================================================================

    @Override
    public List<TransactionResponse> getTransactionsByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        log.info(
                "{} Fetching transactions between {} and {}",
                LogConstants.TRANSACTION_MODULE,
                startDate,
                endDate
        );

        if (startDate.isAfter(endDate)) {
            log.warn(
                    "{} Start date {} is after end date {}",
                    LogConstants.TRANSACTION_MODULE,
                    startDate,
                    endDate
            );
            throw new IllegalArgumentException(
                    "Start date must be before or equal to end date"
            );
        }

        List<TransactionResponse> transactions =
                repository.findByTransactionDateBetween(startDate, endDate)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        log.info(
                "{} Total transactions fetched={} between {} and {}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                startDate,
                endDate
        );

        return transactions;
    }

    @Override
    public List<TransactionResponse> getTransactionsByUserAndDateRange(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {

        log.info(
                "{} Fetching transactions for UserId={} between {} and {}",
                LogConstants.TRANSACTION_MODULE,
                userId,
                startDate,
                endDate
        );

        if (startDate.isAfter(endDate)) {
            log.warn(
                    "{} Start date {} is after end date {}",
                    LogConstants.TRANSACTION_MODULE,
                    startDate,
                    endDate
            );
            throw new IllegalArgumentException(
                    "Start date must be before or equal to end date"
            );
        }

        List<TransactionResponse> transactions =
                repository.findByUserIdAndTransactionDateBetween(
                        userId, startDate, endDate)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        log.info(
                "{} Total transactions fetched={} for UserId={} between {} and {}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                userId,
                startDate,
                endDate
        );

        return transactions;
    }

    // ========================================================================
    //  Recent transactions
    // ========================================================================

    @Override
    public List<TransactionResponse> getRecentTransactions(
            Long userId,
            int limit) {

        log.info(
                "{} Fetching recent {} transactions for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                limit,
                userId
        );

        Pageable pageable = Pageable.ofSize(limit);
        Page<TransactionResponse> page =
                repository.findRecentByUserId(userId, pageable)
                        .map(mapper::toResponse);

        List<TransactionResponse> transactions = page.getContent();

        log.info(
                "{} Recent transactions fetched={} for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.size(),
                userId
        );

        return transactions;
    }

    @Override
    public Page<TransactionResponse> getRecentTransactions(
            Long userId,
            Pageable pageable) {

        log.info(
                "{} Fetching recent transactions for UserId={} with pagination. Page={}, Size={}",
                LogConstants.TRANSACTION_MODULE,
                userId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<TransactionResponse> transactions =
                repository.findRecentByUserId(userId, pageable)
                        .map(mapper::toResponse);

        log.info(
                "{} Recent transaction pages={} for UserId={}",
                LogConstants.TRANSACTION_MODULE,
                transactions.getTotalPages(),
                userId
        );

        return transactions;
    }
}