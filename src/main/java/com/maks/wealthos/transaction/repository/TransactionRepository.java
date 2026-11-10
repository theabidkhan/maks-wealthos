package com.maks.wealthos.transaction.repository;

import com.maks.wealthos.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
        extends JpaRepository<TransactionEntity, Long> {
}
