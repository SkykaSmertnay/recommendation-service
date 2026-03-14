package org.skypro.recommendationservice.rule;

public enum ComparisonType {
    GT(">"),
    LT("<"),
    EQ("="),
    GTE(">="),
    LTE("<=");

    private final String value;

    ComparisonType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ComparisonType fromValue(String value) {
        for (ComparisonType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown comparison type: " + value);
    }
}