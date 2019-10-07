package com.convious.pricingapi;

public class PricingRequestProduct {
    private final String productReference;
    private final int numberOfItems;

    public PricingRequestProduct(String productReference, int numberOfItems) {
        this.productReference = productReference;
        this.numberOfItems = numberOfItems;
    }

    public String productReference() {
        return productReference;
    }

    public int numberOfItems() {
        return numberOfItems;
    }
}
