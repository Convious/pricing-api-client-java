package com.convious.pricingapi.transport;

import java.util.concurrent.CompletableFuture;

public interface AuthService {
    CompletableFuture<String> getAuthToken();
}
