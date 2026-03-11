package org.skypro.recommendationservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(
            @Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getRandomTransactionAmount(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
                Integer.class,
                userId
        );
        return result != null ? result : 0;
    }
    public boolean existsProductType(UUID userId, String productType) {
        Boolean result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) > 0 " +
                        "FROM transactions t " +
                        "JOIN products p ON t.product_id = p.id " +
                        "WHERE t.user_id = ? AND p.type = ?",
                Boolean.class,
                userId,
                productType
        );
        return result != null && result;
    }

    public int getTransactionSum(UUID userId, String productType, String transactionType) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT SUM(t.amount) " +
                        "FROM transactions t " +
                        "JOIN products p ON t.product_id = p.id " +
                        "WHERE t.user_id = ? AND p.type = ? AND t.type = ?",
                Integer.class,
                userId,
                productType,
                transactionType
        );
        return result != null ? result : 0;
    }
}
