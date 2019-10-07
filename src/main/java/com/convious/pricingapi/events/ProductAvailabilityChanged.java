package com.convious.pricingapi.events;

import java.time.LocalDate;

public class ProductAvailabilityChanged implements InventoryEvent {
    private final String productReference;
    private final LocalDate eventDate;
    private final String startTime;
    private final int availability;

    public ProductAvailabilityChanged(String productReference, LocalDate eventDate, String startTime, int availability) {
        this.productReference = productReference;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.availability = availability;
    }

    public String productReference() {
        return productReference;
    }

    public LocalDate eventDate() {
        return eventDate;
    }

    public String startTime() {
        return startTime;
    }

    public int availability() {
        return availability;
    }
}
