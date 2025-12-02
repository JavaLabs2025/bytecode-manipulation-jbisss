package org.example;

import org.example.workers.MetricEnum;

import java.util.List;

public class StatItemDto {

    public static class Item {

        private final String description;
        private final String value;

        public Item(String description, String value) {
            this.description = description;
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public String getValue() {
            return value;
        }
    }

    private final MetricEnum metricType;
    private final List<Item> items;

    public StatItemDto(MetricEnum metricType, List<Item> items) {
        this.metricType = metricType;
        this.items = items;
    }

    public MetricEnum getMetricType() {
        return metricType;
    }

    public List<Item> getItems() {
        return items;
    }
}
