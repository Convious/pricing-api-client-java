package com.convious.pricingapi;

import java.util.Objects;

public class EventEnvelope {
    private final String type;
    private final Object payload;

    public EventEnvelope(Object event) {
        this.type = event.getClass().getSimpleName();
        this.payload = event;
    }

    public String type() {
        return type;
    }

    public Object payload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEnvelope that = (EventEnvelope) o;
        return Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload);
    }
}
