package com.convious.pricingapi;

import com.convious.pricingapi.events.InventoryEvent;
import com.convious.pricingapi.transport.HttpRequest;
import com.convious.pricingapi.transport.HttpResponse;
import com.convious.pricingapi.transport.HttpService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PricingApiTest {
    @Test
    void postEventsMakesCorrectHttpRequest() {
        var http = mock(HttpService.class);
        var configuration = new Configuration(
                "not important",
                "https://" + UUID.randomUUID().toString(),
                "not important"
        );

        var response = new HttpResponse(200, null, new HashMap<>());
        when(http.post(any(HttpRequest.class))).thenReturn(CompletableFuture.completedFuture(response));

        var sut = new PricingApiClient(http, configuration);
        var events = new InventoryEvent[] {
                new InventoryEvent() {},
                new InventoryEvent() {},
                new InventoryEvent() {},
        };
        sut.postEvents(events);

        var arguments = ArgumentCaptor.forClass(HttpRequest.class);
        verify(http, times(1)).post(arguments.capture());
        assertEquals(1, arguments.getAllValues().size());

        var expected = new HttpRequest(
                URI.create(configuration.inventoryEndpoint() + "/events"),
                events,
                new HashMap<>() {{
                    put("Content-Type", List.of("application/json"));
                    put("Accept-Version", List.of(PricingApiClient.inventoryApiVersion));
                }}
        );
        assertEquals(expected, arguments.getValue());
    }

    @Test
    void postEventMakesCorrectHttpRequest() {
        var http = mock(HttpService.class);
        var configuration = new Configuration(
                "not important",
                "https://" + UUID.randomUUID().toString(),
                "not important"
        );

        var response = new HttpResponse(200, null, new HashMap<>());
        when(http.post(any(HttpRequest.class))).thenReturn(CompletableFuture.completedFuture(response));

        var sut = new PricingApiClient(http, configuration);
        var event = new InventoryEvent() {};
        sut.postEvent(event);

        var arguments = ArgumentCaptor.forClass(HttpRequest.class);
        verify(http, times(1)).post(arguments.capture());
        assertEquals(1, arguments.getAllValues().size());

        assertArrayEquals(new InventoryEvent[] { event }, (InventoryEvent[])arguments.getValue().body());
    }

    @Test
    void getPricesMakesCorrectHttpRequest() {
        var http = mock(HttpService.class);
        var configuration = new Configuration(
                "not important",
                "not important",
                "https://" + UUID.randomUUID().toString()
        );

        var response = new HttpResponse(200, null, new HashMap<>());
        when(http.post(any(HttpRequest.class))).thenReturn(CompletableFuture.completedFuture(response));

        var sut = new PricingApiClient(http, configuration);
        var request = new PricingRequest(
                "foo",
                "127.0.0.1",
                LocalDate.now(),
                LocalDate.now(),
                "Europe/Amsterdam",
                new PricingRequestProduct[0],
                null
                );
        sut.getPrices(request);

        var arguments = ArgumentCaptor.forClass(HttpRequest.class);
        verify(http, times(1)).post(arguments.capture());
        assertEquals(1, arguments.getAllValues().size());

        var expected = new HttpRequest(
                URI.create(configuration.pricingEndpoint() + "/api/price/rtp"),
                request,
                new HashMap<>() {{
                    put("Content-Type", List.of("application/json"));
                    put("Accept", List.of("application/json"));
                    put("Accept-Version", List.of(PricingApiClient.pricingApiVersion));
                }}
        );
        assertEquals(expected, arguments.getValue());
    }

    @Test
    void getPricesReturnsCorrectResponse() {
        var http = mock(HttpService.class);
        var configuration = new Configuration(
                "not important",
                "not important",
                "https://" + UUID.randomUUID().toString()
        );

        var expected = new PricingResponse(
                new PricingResponseItem[] {
                        new PricingResponseItem(
                                LocalDate.now(),
                                "10:00:00",
                                new PricingResponseProduct[] {
                                        new PricingResponseProduct("foo", 1, BigDecimal.ONE),
                                        new PricingResponseProduct("bar", 2, BigDecimal.TEN),
                                }
                                ),
                        new PricingResponseItem(
                                LocalDate.now(),
                                "12:00:00",
                                new PricingResponseProduct[] {
                                        new PricingResponseProduct("foo", 1, new BigDecimal("12.00")),
                                        new PricingResponseProduct("bar", 2, new BigDecimal("13.50")),
                                }
                        ),
                }
        );
        var response = new HttpResponse(200, Json.serialize(expected), new HashMap<>());
        when(http.post(any(HttpRequest.class))).thenReturn(CompletableFuture.completedFuture(response));

        var sut = new PricingApiClient(http, configuration);
        var request = new PricingRequest(
                "foo",
                "127.0.0.1",
                LocalDate.now(),
                LocalDate.now(),
                "Europe/Amsterdam",
                new PricingRequestProduct[0],
                null
        );
        var actual = sut.getPrices(request);
        assertEquals(expected, actual);
    }
}
