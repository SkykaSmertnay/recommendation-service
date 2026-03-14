package org.skypro.recommendationservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Cache<String, Boolean> productTypeCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private final Cache<String, Integer> transactionSumCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private final Cache<String, Integer> transactionCountCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

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
        String key = userId + ":" + productType;

        return productTypeCache.get(key, k -> {
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
        });
    }

    public int getTransactionSum(UUID userId, String productType, String transactionType) {
        String key = userId + ":" + productType + ":" + transactionType;

        return transactionSumCache.get(key, k -> {
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
        });
    }

    public int getTransactionCount(UUID userId, String productType) {
        String key = userId + ":" + productType;

        return transactionCountCache.get(key, k -> {
            Integer result = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) " +
                            "FROM transactions t " +
                            "JOIN products p ON t.product_id = p.id " +
                            "WHERE t.user_id = ? AND p.type = ?",
                    Integer.class,
                    userId,
                    productType
            );
            return result != null ? result : 0;
        });
    }

    public boolean compareTransactionSumToValue(UUID userId, String productType, String transactionType, String operator, int value) {
        int sum = getTransactionSum(userId, productType, transactionType);

        return switch (operator) {
            case ">" -> sum > value;
            case "<" -> sum < value;
            case "=" -> sum == value;
            case ">=" -> sum >= value;
            case "<=" -> sum <= value;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    public boolean compareDepositAndWithdraw(UUID userId, String productType, String operator) {
        int depositSum = getTransactionSum(userId, productType, "DEPOSIT");
        int withdrawSum = getTransactionSum(userId, productType, "WITHDRAW");

        return switch (operator) {
            case ">" -> depositSum > withdrawSum;
            case "<" -> depositSum < withdrawSum;
            case "=" -> depositSum == withdrawSum;
            case ">=" -> depositSum >= withdrawSum;
            case "<=" -> depositSum <= withdrawSum;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
