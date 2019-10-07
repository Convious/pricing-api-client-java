package com.convious.pricingapi;

import java.time.LocalDate;

public class PricingRequest {
    private final String cookieId;
    private final String ip;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final String timezone;
    private final PricingRequestProduct[] products;
    private final String[] times;

    public PricingRequest(String cookieId, String ip, LocalDate dateFrom, LocalDate dateTo, String timezone, PricingRequestProduct[] products, String[] times) {
        this.cookieId = cookieId;
        this.ip = ip;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timezone = timezone;
        this.products = products;
        this.times = times;
    }

    public String cookieId() {
        return cookieId;
    }

    public String ip() {
        return ip;
    }

    public LocalDate dateFrom() {
        return dateFrom;
    }

    public LocalDate dateTo() {
        return dateTo;
    }

    public String timezone() {
        return timezone;
    }

    public PricingRequestProduct[] products() {
        return products;
    }

    public String[] times() {
        return times;
    }
}
