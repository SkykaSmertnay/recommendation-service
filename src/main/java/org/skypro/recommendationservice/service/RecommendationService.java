package org.skypro.recommendationservice.service;

import org.skypro.recommendationservice.dto.RecommendationDto;
import org.skypro.recommendationservice.dto.RecommendationResponse;
import org.skypro.recommendationservice.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.skypro.recommendationservice.entity.DynamicRuleEntity;
import org.skypro.recommendationservice.repository.DynamicRuleRepository;
import org.skypro.recommendationservice.rule.DynamicRuleEvaluator;
import org.skypro.recommendationservice.repository.RuleStatsRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleEvaluator dynamicRuleEvaluator;
    private final RuleStatsRepository ruleStatsRepository;

    public RecommendationService(List<RecommendationRuleSet> ruleSets,
                                 DynamicRuleRepository dynamicRuleRepository,
                                 DynamicRuleEvaluator dynamicRuleEvaluator,
                                 RuleStatsRepository ruleStatsRepository) {
        this.ruleSets = ruleSets;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleEvaluator = dynamicRuleEvaluator;
        this.ruleStatsRepository = ruleStatsRepository;
    }

    @Transactional
    public RecommendationResponse getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = ruleSets.stream()
                .map(ruleSet -> ruleSet.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(java.util.stream.Collectors.toList());

        List<DynamicRuleEntity> dynamicRules = dynamicRuleRepository.findAll();

        for (DynamicRuleEntity dynamicRule : dynamicRules) {
            if (dynamicRuleEvaluator.isSatisfied(dynamicRule, userId)) {
                recommendations.add(
                        new RecommendationDto(
                                dynamicRule.getProductId(),
                                dynamicRule.getProductName(),
                                dynamicRule.getProductText()
                        )
                );

                ruleStatsRepository.incrementCountByRule(dynamicRule);
            }
        }

        return new RecommendationResponse(userId, recommendations);
    }
}