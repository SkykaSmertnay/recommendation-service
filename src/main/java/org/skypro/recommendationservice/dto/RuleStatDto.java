package org.skypro.recommendationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleStatDto {

    @JsonProperty("rule_id")
    private Long ruleId;

    @JsonProperty("count")
    private long count;

    public RuleStatDto() {
    }

    public RuleStatDto(Long ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}