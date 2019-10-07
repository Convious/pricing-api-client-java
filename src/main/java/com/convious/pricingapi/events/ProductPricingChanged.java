package com.convious.pricingapi.events;

public class ProductPricingChanged implements InventoryEvent {
    private final String productReference;
    private final ProductPricing pricing;

    public ProductPricingChanged(String productReference, ProductPricing pricing) {
        this.productReference = productReference;
        this.pricing = pricing;
    }

    public String productReference() {
        return productReference;
    }

    public ProductPricing pricing() {
        return pricing;
    }
}
