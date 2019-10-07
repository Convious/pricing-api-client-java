package com.convious.pricingapi.events;

import java.math.BigDecimal;

public class ProductPricing {
    private final BigDecimal minAcceptedPrice;
    private final BigDecimal maxAcceptedPrice;
    private final BigDecimal averageTargetPrice;
    private final BigDecimal boxOfficePrice;

    public ProductPricing(BigDecimal minAcceptedPrice, BigDecimal maxAcceptedPrice, BigDecimal averageTargetPrice, BigDecimal boxOfficePrice) {
        this.minAcceptedPrice = minAcceptedPrice;
        this.maxAcceptedPrice = maxAcceptedPrice;
        this.averageTargetPrice = averageTargetPrice;
        this.boxOfficePrice = boxOfficePrice;
    }

    public BigDecimal minAcceptedPrice() {
        return minAcceptedPrice;
    }

    public BigDecimal maxAcceptedPrice() {
        return maxAcceptedPrice;
    }

    public BigDecimal averageTargetPrice() {
        return averageTargetPrice;
    }

    public BigDecimal boxOfficePrice() {
        return boxOfficePrice;
    }
}