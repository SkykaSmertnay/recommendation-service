package org.skypro.recommendationservice.service;

import org.skypro.recommendationservice.dto.CreateDynamicRuleRequest;
import org.skypro.recommendationservice.dto.DynamicRuleListResponse;
import org.skypro.recommendationservice.dto.DynamicRuleResponse;
import org.skypro.recommendationservice.entity.DynamicRuleEntity;
import org.skypro.recommendationservice.mapper.DynamicRuleMapper;
import org.skypro.recommendationservice.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.skypro.recommendationservice.entity.RuleStatsEntity;
import org.skypro.recommendationservice.repository.RuleStatsRepository;
import org.skypro.recommendationservice.dto.RuleStatDto;
import org.skypro.recommendationservice.dto.RuleStatsResponse;
import org.skypro.recommendationservice.entity.RuleStatsEntity;

import java.util.stream.Collectors;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleMapper dynamicRuleMapper;
    private final RuleStatsRepository ruleStatsRepository;

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository,
                              DynamicRuleMapper dynamicRuleMapper,
                              RuleStatsRepository ruleStatsRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleMapper = dynamicRuleMapper;
        this.ruleStatsRepository = ruleStatsRepository;
    }

    public DynamicRuleResponse createRule(CreateDynamicRuleRequest request) {
        DynamicRuleEntity entity = dynamicRuleMapper.toEntity(request);
        DynamicRuleEntity savedEntity = dynamicRuleRepository.save(entity);

        RuleStatsEntity statsEntity = new RuleStatsEntity(savedEntity, 0);
        ruleStatsRepository.save(statsEntity);

        return dynamicRuleMapper.toResponse(savedEntity);
    }

    public DynamicRuleListResponse getAllRules() {
        List<DynamicRuleResponse> rules = dynamicRuleRepository.findAll().stream()
                .map(dynamicRuleMapper::toResponse)
                .toList();

        return new DynamicRuleListResponse(rules);
    }

    public void deleteRule(Long id) {
        dynamicRuleRepository.deleteById(id);
    }

    public RuleStatsResponse getRuleStats() {
        List<RuleStatDto> stats = dynamicRuleRepository.findAll().stream()
                .map(rule -> {
                    long count = ruleStatsRepository.findByRule(rule)
                            .map(RuleStatsEntity::getCount)
                            .orElse(0L);

                    return new RuleStatDto(rule.getId(), count);
                })
                .collect(Collectors.toList());

        return new RuleStatsResponse(stats);
    }


}