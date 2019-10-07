package com.convious.pricingapi.transport;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private URI uri;
    private Object body;
    Map<String, List<String>> headers;

    public HttpRequest(URI uri, Object body) {
        this(uri, body, new HashMap<>());
    }

    public HttpRequest(URI uri, Object body, Map<String, List<String>> headers) {
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        if (headers == null) {
            throw new IllegalArgumentException("headers cannot be null");
        }

        this.uri = uri;
        this.body = body;
        this.headers = headers;
    }

    public URI uri() {
        return uri;
    }

    public Object body() {
        return body;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return uri.equals(that.uri) &&
                Objects.equals(body, that.body) &&
                headers.equals(that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }
}
