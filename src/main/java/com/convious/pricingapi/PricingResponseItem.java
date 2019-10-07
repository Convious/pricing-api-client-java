package com.convious.pricingapi;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class PricingResponseItem {
    private LocalDate priceDate;
    private String priceTime;
    private  PricingResponseProduct[] products;

    PricingResponseItem() {
    }

    public PricingResponseItem(LocalDate priceDate, String priceTime, PricingResponseProduct[] products) {
        this.priceDate = priceDate;
        this.priceTime = priceTime;
        this.products = products;
    }

    public LocalDate priceDate() {
        return priceDate;
    }

    public String priceTime() {
        return priceTime;
    }

    public PricingResponseProduct[] products() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingResponseItem that = (PricingResponseItem) o;
        return priceDate.equals(that.priceDate) &&
                Objects.equals(priceTime, that.priceTime) &&
                Arrays.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(priceDate, priceTime);
        result = 31 * result + Arrays.hashCode(products);
        return result;
    }
}
