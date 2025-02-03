package com.springapp.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springapp.springapp.enums.OrderStatus;
import com.springapp.springapp.enums.OrderType;
import com.springapp.springapp.enums.TransactionType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_id")
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    @JsonIgnore
    private Stock stock;

    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name="price",nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name="order_type",nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name="order_status",nullable = false)
    private OrderStatus orderStatus;

    @Column(name="transaction_date",nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", user=" + user.getUserId() +
                ", stock=" + stock.getId() +
                ", transactionType=" + transactionType +
                ", quantity=" + quantity +
                ", amount=" + amount +
                ", orderType=" + orderType +
                ", orderStatus=" + orderStatus +
                ", transactionDate=" + transactionDate +
                '}';
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
