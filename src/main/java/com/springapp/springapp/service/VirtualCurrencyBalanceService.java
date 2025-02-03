package com.springapp.springapp.service;

import com.springapp.springapp.entity.User;
import com.springapp.springapp.entity.VirtualCurrencyBalance;
import com.springapp.springapp.repository.VirtualCurrencyBalanceRepository;
import com.springapp.springapp.repository.UserRepository;
import com.springapp.springapp.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Service to manage virtual currency balances for users.
 */
@Service
public class VirtualCurrencyBalanceService {

    private final VirtualCurrencyBalanceRepository balanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public VirtualCurrencyBalanceService(VirtualCurrencyBalanceRepository balanceRepository, UserRepository userRepository) {
        this.balanceRepository = balanceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VirtualCurrencyBalance updateBalance(Integer userId, double amount, TransactionType transactionType) {
        VirtualCurrencyBalance balance = balanceRepository.findByUserUserId(userId);
        User user = userRepository.findByUserId(userId);
        if (balance == null) {
            balance = new VirtualCurrencyBalance();
            balance.setBalance(10000);
            balance.setUser(user);
        }

        if (transactionType == TransactionType.DEBIT && balance.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds to complete the transaction");
        }

        // Update balance
        if (transactionType == TransactionType.CREDIT) {
            balance.setBalance(balance.getBalance() + amount);
        } else {
            balance.setBalance(balance.getBalance() - amount);
        }
        balance.setUser(user);

        return balanceRepository.save(balance); // Save updated balance
    }

}
