package org.skypro.recommendationservice.repository;

import org.skypro.recommendationservice.entity.DynamicRuleEntity;
import org.skypro.recommendationservice.entity.RuleStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RuleStatsRepository extends JpaRepository<RuleStatsEntity, Long> {

    Optional<RuleStatsEntity> findByRule(DynamicRuleEntity rule);

    @Modifying
    @Transactional
    @Query("update RuleStatsEntity rs set rs.count = rs.count + 1 where rs.rule = :rule")
    void incrementCountByRule(DynamicRuleEntity rule);
}