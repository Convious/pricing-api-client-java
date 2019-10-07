package com.convious.pricingapi.events;

public class ProductCreated implements InventoryEvent {
    private final String productReference;
    private final String name;
    private final int availability;
    private final ProductPricing pricing;

    public ProductCreated(String productReference, String name, int availability, ProductPricing pricing) {
        this.productReference = productReference;
        this.name = name;
        this.availability = availability;
        this.pricing = pricing;
    }

    public String productReference() {
        return productReference;
    }

    public String name() {
        return name;
    }

    public int availability() {
        return availability;
    }

    public ProductPricing pricing() {
        return pricing;
    }
}
