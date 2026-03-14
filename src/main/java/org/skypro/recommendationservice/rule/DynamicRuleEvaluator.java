package org.skypro.recommendationservice.rule;

import org.skypro.recommendationservice.entity.DynamicRuleEntity;
import org.skypro.recommendationservice.entity.RuleQueryEntity;
import org.skypro.recommendationservice.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DynamicRuleEvaluator {

    private final RecommendationsRepository recommendationsRepository;

    public DynamicRuleEvaluator(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public boolean isSatisfied(DynamicRuleEntity rule, UUID userId) {
        for (RuleQueryEntity query : rule.getQueries()) {
            boolean result = evaluateQuery(query, userId);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateQuery(RuleQueryEntity query, UUID userId) {
        DynamicQueryType queryType = DynamicQueryType.valueOf(query.getQueryType());

        boolean result = switch (queryType) {
            case USER_OF -> {
                String productType = query.getArguments().get(0);
                yield recommendationsRepository.existsProductType(userId, productType);
            }
            case ACTIVE_USER_OF -> {
                String productType = query.getArguments().get(0);
                yield recommendationsRepository.getTransactionCount(userId, productType) >= 5;
            }
            case TRANSACTION_SUM_COMPARE -> {
                String productType = query.getArguments().get(0);
                String transactionType = query.getArguments().get(1);
                String operator = query.getArguments().get(2);
                int value = Integer.parseInt(query.getArguments().get(3));

                yield recommendationsRepository.compareTransactionSumToValue(
                        userId,
                        productType,
                        transactionType,
                        operator,
                        value
                );
            }
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> {
                String productType = query.getArguments().get(0);
                String operator = query.getArguments().get(1);

                yield recommendationsRepository.compareDepositAndWithdraw(
                        userId,
                        productType,
                        operator
                );
            }
        };

        return query.isNegate() ? !result : result;
    }
}