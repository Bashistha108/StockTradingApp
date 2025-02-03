package com.springapp.springapp.repository;

import com.springapp.springapp.entity.LiveStockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveStockPriceRepository extends JpaRepository<LiveStockPrice, Integer> {
    public LiveStockPrice findByStockId(int stockId);
}