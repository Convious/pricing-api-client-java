package com.convious.pricingapi;

import java.math.BigDecimal;
import java.util.Objects;

public class PricingResponseProduct {
    private String productReference;
    private int numberOfItems;
    private BigDecimal price;

    PricingResponseProduct() {}

    public PricingResponseProduct(String productReference, int numberOfItems, BigDecimal price) {
        this.productReference = productReference;
        this.numberOfItems = numberOfItems;
        this.price = price;
    }

    public String productReference() {
        return productReference;
    }

    public int numberOfItems() {
        return numberOfItems;
    }

    public BigDecimal price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingResponseProduct that = (PricingResponseProduct) o;
        return numberOfItems == that.numberOfItems &&
                productReference.equals(that.productReference) &&
                price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productReference, numberOfItems, price);
    }
}
