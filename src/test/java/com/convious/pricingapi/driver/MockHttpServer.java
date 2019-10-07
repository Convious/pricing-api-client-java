package com.convious.pricingapi.driver;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.*;

class MockResponse {
    private int status;
    private String body;
    private Map<String, List<String>> headers;

    public MockResponse(int status, String body, Map<String, List<String>> headers) {
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
}

public class MockHttpServer {
    private Undertow server;
    private List<PerformedRequest> requests = new ArrayList<>();
    private Queue<MockResponse> responses = new LinkedList<>();
    private int port = 47327;

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private Map<String, List<String>> convertHttpHeaders(HeaderMap requestHeaders) {
        var result = new HashMap<String, List<String>>();
        for (var name : requestHeaders.getHeaderNames()) {
            var key = name.toString().toLowerCase();
            var values = requestHeaders.get(name).toArray();
            result.put(key, Arrays.asList(values));
        }
        return result;
    }

    private Map<String, List<String>> convertQueryParameters(Map<String, Deque<String>> query) {
        var result = new HashMap<String, List<String>>();
        for (var entry : query.entrySet()) {
            result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return result;
    }

    public MockHttpServer() {
        this.server = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(new BlockingHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        requests.add(new PerformedRequest(
                                exchange.getRequestMethod().toString().toUpperCase(),
                                new URI(exchange.getRequestURL()).getPath(),
                                convertQueryParameters(exchange.getQueryParameters()),
                                convertStreamToString(exchange.getInputStream()),
                                convertHttpHeaders(exchange.getRequestHeaders())
                        ));

                        var response = responses.poll();
                        if (response == null) {
                            response = new MockResponse(200, null, new HashMap<>());
                        }

                        exchange.setStatusCode(response.status());

                        for (var header : response.headers().entrySet()) {
                            for (var headerValue : header.getValue()) {
                                exchange.getResponseHeaders().add(new HttpString(header.getKey()), headerValue);
                            }
                        }

                        if (response.body() != null) {
                            exchange.getOutputStream().write(response.body().getBytes());
                        }

                        exchange.endExchange();
                    }
                })).build();
    }

    public int port() {
        return port;
    }

    public PerformedRequest[] requests() {
        return requests.toArray(new PerformedRequest[0]);
    }

    public void replyWith(int status) {
        replyWith(status, null);
    }

    public void replyWith(int status, String body) {
        replyWith(status, body, new HashMap<>());
    }

    public void replyWith(int status, String body, Map<String, List<String>> headers) {
        responses.add(new MockResponse(status, body, headers));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
