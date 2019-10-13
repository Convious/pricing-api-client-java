package com.convious.pricingapi.transport;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AuthenticatedHttpService implements HttpService {
    private final HttpService underlying;
    private final AuthService authService;
    private volatile String authToken = null;

    public AuthenticatedHttpService(HttpService underlying, AuthService authService) {
        this.underlying = underlying;
        this.authService = authService;
    }

    private CompletableFuture<String> ensureLoggedIn(String token) {
        if (token != null) {
            return CompletableFuture.completedFuture(token);
        }

        return authService.getAuthToken().whenComplete((t, exc) -> {
            if (t != null) {
                this.authToken = t;
            }
        });
    }

    private static HttpRequest applyAuthHeader(HttpRequest request, String token) {
        var newHeaders = new HashMap<>(request.headers());
        newHeaders.put("Authorization", List.of("Bearer " + token));
        return new HttpRequest(request.uri(), request.body(), newHeaders);
    }

    @Override
    public CompletableFuture<HttpResponse> post(HttpRequest request) {
        return ensureLoggedIn(authToken)
                .thenCompose(token -> underlying.post(applyAuthHeader(request, token)))
                .thenCompose(response -> {
                    System.setProperty("http.proxyHost", "127.0.0.1");
                    System.setProperty("https.proxyHost", "127.0.0.1");
                    System.setProperty("http.proxyPort", "8888");
                    System.setProperty("https.proxyPort", "8888");
                    if (response.status() == 401) {
                        return ensureLoggedIn(null).thenCompose(token -> underlying.post(applyAuthHeader(request, token)));
                    }
                    ;
                    return CompletableFuture.completedFuture(response);
                });
    }
}
