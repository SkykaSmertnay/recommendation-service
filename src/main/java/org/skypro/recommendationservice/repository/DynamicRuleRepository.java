package org.skypro.recommendationservice.repository;

import org.skypro.recommendationservice.entity.DynamicRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DynamicRuleRepository extends JpaRepository<DynamicRuleEntity, Long> {
}