package org.skypro.recommendationservice.service;

import org.skypro.recommendationservice.dto.CreateDynamicRuleRequest;
import org.skypro.recommendationservice.dto.DynamicRuleListResponse;
import org.skypro.recommendationservice.dto.DynamicRuleResponse;
import org.skypro.recommendationservice.entity.DynamicRuleEntity;
import org.skypro.recommendationservice.mapper.DynamicRuleMapper;
import org.skypro.recommendationservice.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleMapper dynamicRuleMapper;

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository,
                              DynamicRuleMapper dynamicRuleMapper) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleMapper = dynamicRuleMapper;
    }

    public DynamicRuleResponse createRule(CreateDynamicRuleRequest request) {
        DynamicRuleEntity entity = dynamicRuleMapper.toEntity(request);
        DynamicRuleEntity savedEntity = dynamicRuleRepository.save(entity);
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
}