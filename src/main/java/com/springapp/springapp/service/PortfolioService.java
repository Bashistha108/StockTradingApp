package com.springapp.springapp.service;

import com.springapp.springapp.entity.Portfolio;
import com.springapp.springapp.entity.Stock;
import com.springapp.springapp.entity.User;
import com.springapp.springapp.enums.TransactionType;
import com.springapp.springapp.repository.PortfolioRepository;
import com.springapp.springapp.repository.StockRepository;
import com.springapp.springapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final LiveStockPriceService liveStockPriceService;
    private static final Logger logger = LoggerFactory.getLogger(PortfolioService.class);

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository,
                            UserRepository userRepository,
                            StockRepository stockRepository,
                            LiveStockPriceService liveStockPriceService){
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.liveStockPriceService = liveStockPriceService;
    }

    @Transactional
    public void deletePortfolio(Portfolio portfolio) {

        User user = portfolio.getUser(); // Assuming Portfolio has a reference to User
        if (user != null) {
            user.getPortfolios().remove(portfolio); // Remove from user's portfolio list
        }
        portfolioRepository.delete(portfolio);
    }

    public Portfolio findPortfolioByUserIdAndStockId(Integer userId, Integer stockId) {
        return portfolioRepository.findByUserUserIdAndStockId(userId, stockId);
    }

    @Transactional
    public void updatePortfolio(Integer userId, Integer stockId, double quantity, double averagePrice, TransactionType transactionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new NoSuchElementException("Stock not found with id: " + stockId));

        // Find or create the portfolio
        Portfolio portfolio = portfolioRepository.findByUserUserIdAndStockId(userId, stockId);

        if (portfolio == null) {
            // Create a new portfolio if it doesn't exist
            portfolio = new Portfolio();
            portfolio.setUser(user);
            portfolio.setUserId(userId);
            portfolio.setStock(stock);
            portfolio.setStockId(stockId);
            portfolio.setTotalQuantity(quantity);
            portfolio.setAveragePrice(averagePrice);
            logger.info("Saving portfolio: " + portfolio);
        } else {
            if(transactionType == TransactionType.BUY){
                double newQuantity = portfolio.getTotalQuantity() + quantity;
                double newAveragePrice = (portfolio.getAveragePrice() * portfolio.getTotalQuantity() + averagePrice * quantity) / newQuantity;
                portfolio.setTotalQuantity(newQuantity);
                portfolio.setAveragePrice(newAveragePrice);
                logger.info("Updated portfolio: " + portfolio);
            }if (transactionType == TransactionType.SELL) {

                if(portfolio.getTotalQuantity() >= quantity){
                    double newQuantity = portfolio.getTotalQuantity() - quantity;
                    String symbol = portfolio.getStock().getStockSymbol();
                    double currentPrice = liveStockPriceService.getLivePrice(symbol);
                    double ownedStockPrice = portfolio.getAveragePrice();
                    double profitLoss = (currentPrice-ownedStockPrice)*quantity;
                    portfolio.setProfitLoss(profitLoss);
                    portfolio.setTotalQuantity(newQuantity);
                    if(newQuantity == 0.0){
                        portfolio.setAveragePrice(0.0);
                    }

                }else{
                    throw new IllegalArgumentException("Not enough shares to sell.......");
                }
            }

        }
        portfolioRepository.save(portfolio);
        logger.info("Portfolio updated successfully: " + portfolio);
        System.out.println(portfolio);
    }

}



































