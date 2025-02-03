package com.springapp.springapp.controller;

import com.springapp.springapp.entity.LiveStockPrice;
import com.springapp.springapp.service.LiveStockPriceService;
import com.springapp.springapp.service.StockPriceHistoryService;
import com.springapp.springapp.service.StockService;
import com.springapp.springapp.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stocks")
public class StockController {

    private final LiveStockPriceService liveStockPriceService;
    private final StockPriceHistoryService stockPriceHistoryService;
    private final StockService stockService;


    @Autowired
    public StockController(LiveStockPriceService liveStockPriceService, StockPriceHistoryService stockPriceHistoryService, StockService stockService){
        this.liveStockPriceService = liveStockPriceService;
        this.stockPriceHistoryService = stockPriceHistoryService;
        this.stockService = stockService;

    }

    @GetMapping("/add-form")
    public String showFormForAdd(Model model){
        Stock stock = new Stock();
        model.addAttribute("stock",stock);
        return "/stock/stock-add-form";
    }

    @GetMapping("/update-form/{stockId}")
    public String showFormForUpdate(@PathVariable("stockId") int id, Model model){
        Stock stock = stockService.getStockById(id);
        model.addAttribute("stock", stock);
        return "stock/stock-add-form";
    }

    @PostMapping("/add")
    public String addStock(@ModelAttribute Stock stock){
        System.out.println(stock.getId());
        String symbol = stock.getStockSymbol().toUpperCase();
        String name = stock.getStockName().toUpperCase();
        stock.setStockSymbol(symbol);
        stock.setStockName(name);
        stockService.saveAndUpdateStock(stock);
        return "redirect:/stocks/manage-stocks";

    }

    @GetMapping("/manage-stocks")
    public String manageStocks(Model model){
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("stocks", stocks);
        return "stock/stock-manage";
    }

    @GetMapping("/get-all")
    public List<Stock> getAllStocks(){
        return stockService.getAllStocks();
    }


    @GetMapping("/delete-stock/{id}")
    public String deleteStockById(@PathVariable int id){
        stockService.deleteStock(id);
        return "redirect:/stocks/manage-stocks";
    }


    @PostMapping("/search")
    public String searchStock(@RequestParam("symbol") String symbol, Model model){

        LiveStockPrice liveStockPrice = liveStockPriceService.getLiveStockPriceEntity(symbol);
        //update the live price in the db
        liveStockPriceService.updateLiveStockPrice(symbol);
        //Retrieve and return the updated live price
        double currentPrice = liveStockPrice.getCurrentPrice();
        int stockId = stockService.getStockIdBySymbol(symbol);
        model.addAttribute("currentPrice", currentPrice);
        model.addAttribute("symbol", symbol);
        model.addAttribute("stockId", stockId);
        return "stock/stock-price";
    }

    @PostMapping("/update/{symbol}")
    public String updateHistoricalData(@PathVariable("symbol") String symbol) {
        try {
            stockPriceHistoryService.fetchAndStoreHistoricalData(symbol);
            System.out.println("Historical data updated successfully for the symbol: "+symbol);
        } catch (Exception e){
            System.out.println("Failed to update historical data for symbol: "+symbol+"-"+e.getMessage());
        }
        return "redirect:/stocks/search/"+symbol;
    }

    @GetMapping("/search/{symbol}")
    public String showStockPricePageSupporter(@PathVariable("symbol") String symbol, Model model){
        double currentPrice = liveStockPriceService.getLivePrice(symbol);
        liveStockPriceService.updateLiveStockPrice(symbol);
        model.addAttribute("currentPrice", currentPrice);
        model.addAttribute("symbol", symbol);
        return "stock/stock-price";
    }

}




















