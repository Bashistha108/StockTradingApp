package com.springapp.springapp.entity;

import java.io.Serializable;
import java.util.Objects;

public class PortfolioId implements Serializable {

    private Integer userId;
    private Integer stockId;


    public PortfolioId() {}

    public PortfolioId(Integer userId, Integer stockId) {
        this.userId = userId;
        this.stockId = stockId;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getStockId() {
        return stockId;
    }
    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }


    /**
     * Ensures correct comparison in databases and collections.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioId that = (PortfolioId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(stockId, that.stockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, stockId);
    }
}
