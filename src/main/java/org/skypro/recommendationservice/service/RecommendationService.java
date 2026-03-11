package org.skypro.recommendationservice.service;

import org.skypro.recommendationservice.dto.RecommendationDto;
import org.skypro.recommendationservice.dto.RecommendationResponse;
import org.skypro.recommendationservice.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    public RecommendationResponse getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = ruleSets.stream()
                .map(ruleSet -> ruleSet.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return new RecommendationResponse(userId, recommendations);
    }
}