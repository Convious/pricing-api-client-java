package com.convious.pricingapi.transport;

import java.util.concurrent.CompletableFuture;

public interface HttpService {
    CompletableFuture<HttpResponse> post(HttpRequest request);
}
