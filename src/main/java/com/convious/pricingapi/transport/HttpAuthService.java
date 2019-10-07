package com.convious.pricingapi.transport;

import com.convious.pricingapi.Json;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class HttpAuthService implements AuthService {
    private final URI authEndpoint;
    private final String clientId;
    private final String clientSecret;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public HttpAuthService(String authEndpoint, String clientId, String clientSecret) {
        this.authEndpoint = URI.create(authEndpoint + "/oauth/token/");
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private String extractAuthToken(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new AuthenticationException("Authentication has failed: " + response.body());
        }

        var json = Json.deserialize(response.body(), JsonObject.class);
        return json.get("access_token").getAsString();
    }

    @Override
    public CompletableFuture<String> getAuthToken() {
        var postData = String.format(
                "grant_type=client_credentials&client_id=%s&client_secret=%s",
                URLEncoder.encode(clientId, StandardCharsets.UTF_8),
                URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
                );

        var request = HttpRequest.newBuilder()
                .uri(authEndpoint)
                .POST(HttpRequest.BodyPublishers.ofString(postData))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::extractAuthToken);
    }
}
