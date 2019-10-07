package com.convious.pricingapi.transport;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private int status;
    private String body;
    private Map<String,List<String>> headers;

    public HttpResponse(int status, String body, Map<String, List<String>> headers) {
        if (headers == null) {
            throw new IllegalArgumentException("headers cannot be null");
        }
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public int status() {
        return status;
    }

    public String body() {
        return body;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpResponse that = (HttpResponse) o;
        return status == that.status &&
                Objects.equals(body, that.body) &&
                headers.equals(that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, body);
    }
}
