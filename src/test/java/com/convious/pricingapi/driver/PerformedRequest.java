package com.convious.pricingapi.driver;

import java.util.Deque;
import java.util.List;
import java.util.Map;

public class PerformedRequest {
    private String method;
    private String url;
    private Map<String, List<String>> query;
    private String body;
    private Map<String, List<String>> headers;

    public PerformedRequest(String method, String url, Map<String, List<String>> query, String body, Map<String, List<String>> headers) {
        this.method = method;
        this.url = url;
        this.query = query;
        this.body = body;
        this.headers = headers;
    }

    public String method() {
        return method;
    }

    public String url() {
        return url;
    }

    public Map<String, List<String>> query() {
        return query;
    }

    public String body() {
        return body;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }
}