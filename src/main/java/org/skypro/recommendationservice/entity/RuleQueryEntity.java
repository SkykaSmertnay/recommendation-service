package org.skypro.recommendationservice.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rule_query")
public class RuleQueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_type", nullable = false)
    private String queryType;

    @ElementCollection
    @CollectionTable(name = "rule_query_argument", joinColumns = @JoinColumn(name = "rule_query_id"))
    @Column(name = "argument", nullable = false)
    private List<String> arguments = new ArrayList<>();

    @Column(name = "negate", nullable = false)
    private boolean negate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private DynamicRuleEntity rule;

    public RuleQueryEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public DynamicRuleEntity getRule() {
        return rule;
    }

    public void setRule(DynamicRuleEntity rule) {
        this.rule = rule;
    }
}