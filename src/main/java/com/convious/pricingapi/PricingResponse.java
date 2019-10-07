package com.convious.pricingapi;

import java.util.Arrays;

public class PricingResponse {
    private PricingResponseItem[] prices;

    PricingResponse() {}

    public PricingResponse(PricingResponseItem[] prices) {
        this.prices = prices;
    }

    public PricingResponseItem[] prices() {
        return prices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingResponse that = (PricingResponse) o;
        return Arrays.equals(prices, that.prices);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(prices);
    }
}
