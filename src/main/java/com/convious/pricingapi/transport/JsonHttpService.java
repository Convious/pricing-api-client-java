package com.convious.pricingapi.transport;

import com.convious.pricingapi.Json;
import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;


public class JsonHttpService implements HttpService {
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public CompletableFuture<HttpResponse> post(HttpRequest request) {
        var httpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(request.uri())
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(Json.serialize(request.body())));

        for (var header : request.headers().entrySet()) {
            for (var headerValue : header.getValue()) {
                httpRequest = httpRequest.header(header.getKey(), headerValue);
            }
        }

        return client.sendAsync(httpRequest.build(), java.net.http.HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> new HttpResponse(
                        resp.statusCode(),
                        resp.body(),
                        resp.headers().map()
                ));
    }
}
