package org.skypro.recommendationservice.controller;

import org.skypro.recommendationservice.dto.CreateDynamicRuleRequest;
import org.skypro.recommendationservice.dto.DynamicRuleListResponse;
import org.skypro.recommendationservice.dto.DynamicRuleResponse;
import org.skypro.recommendationservice.service.DynamicRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.skypro.recommendationservice.dto.RuleStatsResponse;

@RestController
public class RuleController {

    private final DynamicRuleService dynamicRuleService;

    public RuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    @PostMapping("/rule")
    public DynamicRuleResponse createRule(@RequestBody CreateDynamicRuleRequest request) {
        return dynamicRuleService.createRule(request);
    }

    @GetMapping("/rule")
    public DynamicRuleListResponse getAllRules() {
        return dynamicRuleService.getAllRules();
    }

    @DeleteMapping("/rule/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        dynamicRuleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rule/stats")
    public RuleStatsResponse getRuleStats() {
        return dynamicRuleService.getRuleStats();
    }
}