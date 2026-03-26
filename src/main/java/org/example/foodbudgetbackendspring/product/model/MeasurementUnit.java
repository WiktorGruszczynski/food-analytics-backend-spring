package org.example.foodbudgetbackendspring.product.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MeasurementUnit {
    GRAM("g"),
    MILLILITER("ml");

    private final String label;

    MeasurementUnit(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
