package org.skypro.recommendationservice.rule;

import org.skypro.recommendationservice.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<RecommendationDto> check(UUID userId);
}