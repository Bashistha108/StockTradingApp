package com.springapp.springapp.controller;


import com.springapp.springapp.entity.Portfolio;
import com.springapp.springapp.entity.User;
import com.springapp.springapp.repository.StockRepository;
import com.springapp.springapp.repository.UserRepository;
import com.springapp.springapp.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @GetMapping("/{userId}/{stockId}")
    public Portfolio getPortfolio(@PathVariable Integer userId, @PathVariable Integer stockId) {
        return portfolioService.findPortfolioByUserIdAndStockId(userId, stockId);
    }


    @GetMapping("/")
    public String usersPortfolio(Model model){
        // Get the logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // or authentication.getPrincipal() if you're using a custom user principal

        // Find the user by username
        User user = userRepository.findByEmail(email);
        List<Portfolio> allPortfolios = user.getPortfolios();
        System.out.println(allPortfolios);
        int totalProfitLoss = 0;
        List<Portfolio> portfolios = new ArrayList<>();
        for(Portfolio portfolio : allPortfolios){
            totalProfitLoss += portfolio.getProfitLoss();
            if(portfolio.getTotalQuantity() > 0.0){
                portfolios.add(portfolio);
            }
        }
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("totalProfitLoss", totalProfitLoss);
        return "user/portfolio";
    }




























}
