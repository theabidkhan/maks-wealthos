package com.maks.wealthos.transaction.mapper;

import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;
import com.maks.wealthos.transaction.dto.UpdateTransactionRequest;
import com.maks.wealthos.transaction.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Map CreateTransactionRequest to TransactionEntity
     */
    TransactionEntity toEntity(
            CreateTransactionRequest request
    );

    /**
     * Map UpdateTransactionRequest to TransactionEntity
     */
    TransactionEntity toEntity(
            UpdateTransactionRequest request
    );

    /**
     * Update existing TransactionEntity from UpdateTransactionRequest
     */
    void updateEntity(
            UpdateTransactionRequest request,
            @MappingTarget TransactionEntity entity
    );

    /**
     * Map TransactionEntity to TransactionResponse
     */
    TransactionResponse toResponse(
            TransactionEntity entity
    );
}