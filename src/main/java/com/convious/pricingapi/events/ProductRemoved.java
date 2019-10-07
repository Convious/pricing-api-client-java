package com.convious.pricingapi.events;

public class ProductRemoved implements InventoryEvent {
    private final String productReference;

    public ProductRemoved(String productReference) {
        this.productReference = productReference;
    }

    public String productReference() {
        return productReference;
    }
}
