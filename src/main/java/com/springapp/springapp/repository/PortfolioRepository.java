package com.springapp.springapp.repository;

import com.springapp.springapp.entity.Portfolio;
import com.springapp.springapp.entity.PortfolioId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, PortfolioId> {
    // Find all portfolios for a specific user
    // findBy + user + userId
    List<Portfolio> findByUserUserId(Integer userId);

    /**
     * Find a specific portfolio entry by user and stock
     * Stock entity field -> id so only id
     * User entity field -> userId so userId
     * findByUserUserIdAndStockId -> findBy + user.userId And stock.id
     */
    Portfolio findByUserUserIdAndStockId(Integer userId, Integer stockId);
    // Alternatively
    // @Query("SELECT p FROM Portfolio p WHERE p.user.userId = :userId AND p.stock.stockId = :stockId")
    // Portfolio findPortfolioByUserIdAndStockId(@Param("userId") Integer userId, @Param("stockId") Integer stockId);
}
