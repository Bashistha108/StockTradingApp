package com.springapp.springapp.repository;

import com.springapp.springapp.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// JpaRepository<Stock(X), Integer> ----> findBy..... it searches for Stock(X)
@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    // findBy + StockSymbol(because Stock entity has stockSymbol attribute)
    Stock findByStockSymbol(String symbol);
    Stock findByStockName(String name);

}
