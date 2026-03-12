package org.skypro.recommendationservice.dto;

import java.util.List;

public class DynamicRuleListResponse {

    private List<DynamicRuleResponse> data;

    public DynamicRuleListResponse() {
    }

    public DynamicRuleListResponse(List<DynamicRuleResponse> data) {
        this.data = data;
    }

    public List<DynamicRuleResponse> getData() {
        return data;
    }

    public void setData(List<DynamicRuleResponse> data) {
        this.data = data;
    }
}