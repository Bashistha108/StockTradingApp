package com.springapp.springapp.service;

import com.springapp.springapp.entity.Stock;
import com.springapp.springapp.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;



@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService( StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public Stock getStockById(int id){
        return stockRepository.findById(id).orElseThrow(()->new NoSuchElementException());
    }


    public Stock getStockBySymbol(String symbol){
        return stockRepository.findByStockSymbol(symbol);
    }

    public Stock getStockByName(String name){
        return stockRepository.findByStockName(name);
    }
    public List<Stock> getAllStocks(){
        return stockRepository.findAll();
    }

    public int getStockIdBySymbol(String symbol){
        return stockRepository.findByStockSymbol(symbol).getId();
    }

    public Stock addStock(Stock stock){
        return stockRepository.save(stock);
    }

    public Stock updateStock(int id, Stock stock){
        if(stockRepository.existsById(id)){
            stock.setId(id);
            return stockRepository.save(stock);
        }
        return null;
    }

    public void saveAndUpdateStock(Stock stock){
        //Check if the stock exists
        if(stock.getId() != null){
            Stock tempStock = getStockById(stock.getId());
            tempStock.setStockName(stock.getStockName());
            tempStock.setStockSymbol(stock.getStockSymbol());
            stockRepository.save(stock);
            System.out.println("Stock updated with id: "+stock.getId());
        }else{
            stockRepository.save(stock);
        }
    }

    public boolean deleteStock(int id){
        if(stockRepository.existsById(id)){
            stockRepository.deleteById(id);
            return true;
        }

        return false;
    }



}
