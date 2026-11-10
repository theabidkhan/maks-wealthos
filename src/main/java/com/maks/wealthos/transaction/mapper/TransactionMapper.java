package com.maks.wealthos.transaction.mapper;

import com.maks.wealthos.transaction.dto.CreateTransactionRequest;
import com.maks.wealthos.transaction.dto.TransactionResponse;
import com.maks.wealthos.transaction.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionEntity toEntity(
            CreateTransactionRequest request
    );

    TransactionResponse toResponse(
            TransactionEntity entity
    );
}