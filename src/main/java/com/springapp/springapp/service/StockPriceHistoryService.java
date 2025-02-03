package com.springapp.springapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.springapp.springapp.entity.Stock;
import com.springapp.springapp.entity.StockPriceHistory;
import com.springapp.springapp.repository.StockPriceHistoryRepository;
import com.springapp.springapp.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class StockPriceHistoryService {

    private final StockRepository stockRepository;
    private final AlphaVantageClient alphaVantageClient;
    private final StockPriceHistoryRepository stockPriceHistoryRepository;


    public StockPriceHistoryService(StockRepository stockRepository, AlphaVantageClient alphaVantageClient, StockPriceHistoryRepository stockPriceHistoryRepository){
        this.stockRepository = stockRepository;
        this.alphaVantageClient = alphaVantageClient;
        this.stockPriceHistoryRepository = stockPriceHistoryRepository;
    }


    /**
     * Fetches and stores historical data for a stock using AlphaVantage API
     */
    public void fetchAndStoreHistoricalData(String stockSymbol) {
        //Fetch the Stock entity by symbol
        Stock stock = stockRepository.findByStockSymbol(stockSymbol);
        if (stock == null) {
            System.err.println("Stock with symbol " + stockSymbol + " not found.");
            return;
        }

        JsonNode timeSeriesData = alphaVantageClient.getHistoricalData(stockSymbol);
        if (timeSeriesData != null && timeSeriesData.isObject()) {
            timeSeriesData.fields().forEachRemaining(entry -> {
                String dateStr = entry.getKey();
                JsonNode dailyData = entry.getValue();

                // Check if the record already exists in the DB for the same stock and date
                if (stockPriceHistoryRepository.existsByStockIdAndDate(stock.getId(), Date.valueOf(dateStr))) {
                    System.out.println("Historical data for " + stockSymbol + " on " + dateStr + " already exists. Skipping.");
                    return; // Skip saving if record exists
                }

                // Create a new StockPriceHistory record
                StockPriceHistory history = new StockPriceHistory();
                history.setStockId(stock.getId()); // Set the stockId as the foreign key
                history.setDate(Date.valueOf(dateStr)); // Set the date
                history.setOpenPrice(dailyData.path("1. open").asDouble());
                history.setClosePrice(dailyData.path("4. close").asDouble());
                history.setHighPrice(dailyData.path("2. high").asDouble());
                history.setLowPrice(dailyData.path("3. low").asDouble());
                history.setVolume(dailyData.path("5. volume").asLong());

                // Save the record in the repository
                stockPriceHistoryRepository.save(history);
            });
        }
    }



    public List<StockPriceHistory> getStockPriceHistory(int stockId) {
        return stockPriceHistoryRepository.findByStockId(stockId);
    }
}
