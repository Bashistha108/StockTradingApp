package com.springapp.springapp.repository;

import com.springapp.springapp.entity.VirtualCurrencyBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualCurrencyBalanceRepository extends JpaRepository<VirtualCurrencyBalance, Integer> {
    VirtualCurrencyBalance findByUserUserId(Integer userId);
}