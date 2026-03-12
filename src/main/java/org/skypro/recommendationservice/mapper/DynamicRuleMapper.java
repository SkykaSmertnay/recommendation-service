package org.skypro.recommendationservice.mapper;

import org.skypro.recommendationservice.dto.CreateDynamicRuleRequest;
import org.skypro.recommendationservice.dto.DynamicRuleResponse;
import org.skypro.recommendationservice.dto.RuleQueryDto;
import org.skypro.recommendationservice.entity.DynamicRuleEntity;
import org.skypro.recommendationservice.entity.RuleQueryEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DynamicRuleMapper {

    public DynamicRuleEntity toEntity(CreateDynamicRuleRequest request) {
        DynamicRuleEntity entity = new DynamicRuleEntity();
        entity.setProductName(request.getProductName());
        entity.setProductId(request.getProductId());
        entity.setProductText(request.getProductText());

        List<RuleQueryEntity> queries = new ArrayList<>();
        for (RuleQueryDto queryDto : request.getRule()) {
            RuleQueryEntity queryEntity = new RuleQueryEntity();
            queryEntity.setQueryType(queryDto.getQuery());
            queryEntity.setArguments(queryDto.getArguments());
            queryEntity.setNegate(queryDto.isNegate());
            queryEntity.setRule(entity);
            queries.add(queryEntity);
        }

        entity.setQueries(queries);
        return entity;
    }

    public DynamicRuleResponse toResponse(DynamicRuleEntity entity) {
        List<RuleQueryDto> ruleDtos = new ArrayList<>();

        for (RuleQueryEntity queryEntity : entity.getQueries()) {
            RuleQueryDto dto = new RuleQueryDto();
            dto.setQuery(queryEntity.getQueryType());
            dto.setArguments(queryEntity.getArguments());
            dto.setNegate(queryEntity.isNegate());
            ruleDtos.add(dto);
        }

        return new DynamicRuleResponse(
                entity.getId(),
                entity.getProductName(),
                entity.getProductId(),
                entity.getProductText(),
                ruleDtos
        );
    }
}