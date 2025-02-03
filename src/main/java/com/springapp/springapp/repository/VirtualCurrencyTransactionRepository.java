package com.springapp.springapp.repository;

import com.springapp.springapp.entity.VirtualCurrencyTransaction;
import com.springapp.springapp.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualCurrencyTransactionRepository extends JpaRepository<VirtualCurrencyTransaction, Integer> {
    // Find all transactions for a specific user
    List<VirtualCurrencyTransaction> findByUserUserId(Integer userId);

    // Find transactions by type (CREDIT or DEBIT) for a user
    List<VirtualCurrencyTransaction> findByUserUserIdAndTransactionType(Integer userId, TransactionType transactionType);
}
