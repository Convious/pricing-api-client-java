package com.convious.pricingapi;

import com.convious.pricingapi.events.InventoryEvent;
import com.convious.pricingapi.transport.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PricingApiClient {
    private final HttpService httpService;
    private final Configuration configuration;
    public static final String pricingApiVersion = "1.0.0";
    public static final String inventoryApiVersion  = "1.0.0";

    public PricingApiClient(HttpService httpService, Configuration configuration) {
        this.httpService = httpService;
        this.configuration = configuration;
    }

    public static PricingApiClient create(String clientId, String clientSecret) {
        var config = Configuration.defaultConfiguration();
        return new PricingApiClient(
                new AuthenticatedHttpService(
                        new JsonHttpService(),
                        new HttpAuthService(config.authEndpoint(), clientId, clientSecret)
                ),
                config
        );
    }

    private <T> CompletableFuture<T> post(HttpRequest request, Class<T> cls) {
        return httpService.post(request)
                .thenApply(response -> {
                    if (response.status() < 200 || response.status() > 299) {
                        throw new PricingClientException(String.format("Request to %s returned status code %d and body %s", request.uri(), response.status(), response.body()));
                    }
                    return Json.deserialize(response.body(), cls);
                });
    }

    public CompletableFuture postEventsAsync(InventoryEvent[] events) {
        return post(
                new HttpRequest(
                    URI.create(configuration.inventoryEndpoint() + "/events"),
                    events,
                    new HashMap<>() {{
                        put("Content-Type", List.of("application/json"));
                        put("Accept-Version", List.of(inventoryApiVersion));
                    }}
                ),
                Void.class
        );
    }

    public CompletableFuture postEventAsync(InventoryEvent event) {
        return postEventsAsync(new InventoryEvent[] { event });
    }

    public void postEvents(InventoryEvent[] events) {
        postEventsAsync(events).join();
    }

    public void postEvent(InventoryEvent event) {
        postEventAsync(event).join();
    }

    public CompletableFuture<PricingResponse> getPricesAsync(PricingRequest request) {
        return post(
                new HttpRequest(
                        URI.create(configuration.pricingEndpoint() + "/api/price/rtp"),
                        request,
                        new HashMap<>() {{
                            put("Content-Type", List.of("application/json"));
                            put("Accept", List.of("application/json"));
                            put("Accept-Version", List.of(pricingApiVersion));
                        }}
                ),
                PricingResponse.class
        );
    }

    public PricingResponse getPrices(PricingRequest request) {
        return getPricesAsync(request).join();
    }
}
