package com.springapp.springapp.repository;

import com.springapp.springapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // findBy + User + user_id
    // Uses the mapping for User in Transaction Entity to find using user id
    List<Transaction> findByUser_UserId(int userId);
}