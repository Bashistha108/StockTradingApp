package com.springapp.springapp.controller;

import com.springapp.springapp.entity.LiveStockPrice;
import com.springapp.springapp.entity.Stock;
import com.springapp.springapp.entity.User;
import com.springapp.springapp.enums.OrderType;
import com.springapp.springapp.enums.TransactionType;
import com.springapp.springapp.repository.UserRepository;
import com.springapp.springapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private StockService stockService;
    @Autowired
    private LiveStockPriceService liveStockPriceService;
    @Autowired
    private VirtualCurrencyBalanceService virtualCurrencyBalanceService;

    @GetMapping("/order-form")
    public String showFormForOrder(Model model, @RequestParam("stockId") int stockId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        int userId = user.getUserId();
        Stock stock = stockService.getStockById(stockId);
        String symbol = stockService.getStockById(stockId).getStockSymbol();
        liveStockPriceService.updateLiveStockPrice(symbol);
        LiveStockPrice liveStockPrice = liveStockPriceService.getLiveStockPriceEntity(symbol);
        double currentPrice = liveStockPrice.getCurrentPrice();
        model.addAttribute("currentPrice", currentPrice);
        model.addAttribute("userId", userId);
        model.addAttribute("stockId", stockId);
        model.addAttribute("stock", stock);
        liveStockPriceService.updateLiveStockPrice(symbol);
        return "stock/order-form";
    }


    @PostMapping("/buy-sell")
    public String handleTransaction(@RequestParam int userId,
                                    @RequestParam int stockId,
                                    @RequestParam(required = false) Double quantity,
                                    @RequestParam(required = false) Double amount,
                                    @RequestParam TransactionType transactionType,
                                    @RequestParam double currentPrice) {
        try {
            if (currentPrice <= 0) {
                throw new IllegalArgumentException("Stock price is invalid or not available.");
            }

            double totalAmount = 0.0;
            if (quantity != null && quantity > 0) {
                totalAmount = quantity * currentPrice;
            } else if (amount != null && amount > 0) {
                totalAmount = amount;  // Use amount directly
            }

            double averagePrice = currentPrice;

            // Create the transaction (either BUY or SELL)
            transactionService.createTransaction(userId, stockId, transactionType, quantity, amount, OrderType.MARKET);
            // Update the user's portfolio
            portfolioService.updatePortfolio(userId, stockId, quantity, averagePrice, transactionType);

            // Debit or Credit the user's virtual currency balance based on transaction type
            if (transactionType == TransactionType.BUY) {
                virtualCurrencyBalanceService.updateBalance(userId, totalAmount, TransactionType.DEBIT);
                System.out.println("Stock bought successfully");


            } else if (transactionType == TransactionType.SELL) {
                virtualCurrencyBalanceService.updateBalance(userId, totalAmount, TransactionType.CREDIT);
                System.out.println("Stock sold successfully");

            }
            return "redirect:/portfolio/";  // Redirect after successful transaction
        } catch (Exception e) {
            return "Error occurred during transaction: " + e.getMessage();
        }
    }


    @GetMapping("/history/{userId}")
    public Object getTransactionHistory(@PathVariable int userId) {
        try {
            return transactionService.getTransactionHistory(userId);
        } catch (Exception e) {
            return "Error retrieving transaction history: " + e.getMessage();
        }
    }
}
