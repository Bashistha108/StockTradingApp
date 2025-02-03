package com.springapp.springapp.service;

import com.springapp.springapp.entity.Stock;
import com.springapp.springapp.entity.Transaction;
import com.springapp.springapp.entity.User;
import com.springapp.springapp.enums.OrderStatus;
import com.springapp.springapp.enums.OrderType;
import com.springapp.springapp.enums.TransactionType;
import com.springapp.springapp.repository.StockRepository;
import com.springapp.springapp.repository.TransactionRepository;
import com.springapp.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, StockRepository stockRepository, UserRepository userRepository){
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Transaction createTransaction(Integer userId, Integer stockId, TransactionType transactionType,
                                         Double quantity, Double amount, OrderType orderType) {
        User user = userRepository.findByUserId(userId);
        Stock stock = stockRepository.findById(stockId).orElseThrow(()->new NoSuchElementException("Stock not found with id: "+stockId));

        if(quantity==null || quantity <= 0 || amount <= 0){
            throw new IllegalArgumentException("Cannot be like given");
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setStock(stock);
        transaction.setTransactionType(transactionType);
        transaction.setQuantity(quantity);
        transaction.setAmount(amount);
        transaction.setOrderType(orderType);
        transaction.setOrderStatus(OrderStatus.PENDING);  // You can update status later if needed
        System.out.println(transaction.toString());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(int userId) {
        return transactionRepository.findByUser_UserId(userId);
    }
}











