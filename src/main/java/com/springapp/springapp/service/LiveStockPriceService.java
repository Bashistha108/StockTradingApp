package com.springapp.springapp.service;

import com.springapp.springapp.entity.LiveStockPrice;
import com.springapp.springapp.entity.Stock;
import com.springapp.springapp.repository.LiveStockPriceRepository;
import com.springapp.springapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class LiveStockPriceService {

    private final FinnhubClient finnhubClient;
    private final LiveStockPriceRepository liveStockPriceRepository;
    private final StockRepository stockRepository;



    @Autowired
    public LiveStockPriceService(FinnhubClient finnhubClient, LiveStockPriceRepository liveStockPriceRepository, StockRepository stockRepository){
        this.finnhubClient = finnhubClient;
        this.liveStockPriceRepository = liveStockPriceRepository;
        this.stockRepository = stockRepository;
    }



    /**
     * Retrieves LiveStockPrice (Entity) from the repository
     */
    public LiveStockPrice getLiveStockPriceEntity(String stockSymbol) {
        Stock stock = stockRepository.findByStockSymbol(stockSymbol);
        updateLiveStockPrice(stockSymbol);
        if(stock != null){
            return  liveStockPriceRepository.findByStockId(stock.getId());
        }
       return null;
    }

    /**
     * Updates live stock price using finhubb's api also in the db
     */
    @Transactional
    public void updateLiveStockPrice(String stockSymbol){
        Stock stock = stockRepository.findByStockSymbol(stockSymbol);
        if(stock == null){
            System.err.println("Stock with symbol "+stockSymbol+" not found.");
            return;
        }
        double livePrice = 0.0;
        livePrice = finnhubClient.getLivePrice(stockSymbol);

        //Find LiveStockPrice by Stock Id
        LiveStockPrice liveStockPrice = liveStockPriceRepository.findByStockId(stock.getId());

        if (liveStockPrice == null) {
            // Create a new entry if not found
            liveStockPrice = new LiveStockPrice();
        }

        liveStockPrice.setStockId(stock.getId());
        liveStockPrice.setCurrentPrice(livePrice);
        liveStockPrice.setLastUpdated(String.valueOf(new Timestamp(System.currentTimeMillis())));

        //Update in db
        liveStockPriceRepository.save(liveStockPrice);

    }

    public double getLivePrice(String stockSymbol){
        //Update the LiveStockPrice Entity in the db and then
        double livePrice = finnhubClient.getLivePrice(stockSymbol);
        updateLiveStockPrice(stockSymbol);
        return livePrice;
    }



}
