package com.convious.pricingapi;

import java.util.Objects;

public class EventEnvelope {
    private final String type;
    private final Object envelope;

    public EventEnvelope(Object event) {
        this.type = event.getClass().getName();
        this.envelope = event;
    }

    public String type() {
        return type;
    }

    public Object envelope() {
        return envelope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEnvelope that = (EventEnvelope) o;
        return Objects.equals(envelope, that.envelope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(envelope);
    }
}
