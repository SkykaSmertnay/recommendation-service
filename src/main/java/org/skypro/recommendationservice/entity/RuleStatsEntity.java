package org.skypro.recommendationservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rule_stats")
public class RuleStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false, unique = true)
    private DynamicRuleEntity rule;

    @Column(name = "count", nullable = false)
    private long count;

    public RuleStatsEntity() {
    }

    public RuleStatsEntity(DynamicRuleEntity rule, long count) {
        this.rule = rule;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public DynamicRuleEntity getRule() {
        return rule;
    }

    public void setRule(DynamicRuleEntity rule) {
        this.rule = rule;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}