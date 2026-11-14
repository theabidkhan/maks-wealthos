package com.maks.wealthos.transaction.service.impl;

import com.maks.wealthos.common.constants.LogConstants;
import com.maks.wealthos.common.exception.ResourceNotFoundException;
import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;
import com.maks.wealthos.transaction.entity.TransactionEntity;
import com.maks.wealthos.transaction.mapper.TransactionMapper;
import com.maks.wealthos.transaction.repository.TransactionRepository;
import com.maks.wealthos.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

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
    public List<TransactionResponse> getAllTransactions() {

        log.info("{} Fetching all transactions",LogConstants.TRANSACTION_MODULE);

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
}