package com.convious.pricingapi.transport;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class AuthenticatedHttpServiceTest {
    @Test
    void getsAuthTokenAndAuthorizesRequest() {
        var http = mock(HttpService.class);
        var auth = mock(AuthService.class);
        var sut = new AuthenticatedHttpService(http, auth);

        var authToken = "some auth value";
        var request = new HttpRequest(URI.create("http://whatever"), "foo");
        var response = new HttpResponse(203, "response", new HashMap<>());

        when(http.post(any(HttpRequest.class))).thenReturn(CompletableFuture.completedFuture(response));
        when(auth.getAuthToken()).thenReturn(CompletableFuture.completedFuture(authToken));

        var actualResponse = sut.post(request).join();
        assertEquals(response, actualResponse);

        var arguments = ArgumentCaptor.forClass(HttpRequest.class);
        verify(http, times(1)).post(arguments.capture());

        var actualRequest = arguments.getValue();
        assertEquals(request.uri(), actualRequest.uri());
        assertEquals(request.body(), actualRequest.body());
        assertArrayEquals(new String[] {"Bearer some auth value"}, actualRequest.headers().get("Authorization").toArray());
    }

    @Test
    void onlyGetsAuthTokenOnce() {
        var http = mock(HttpService.class);
        var auth = mock(AuthService.class);
        var sut = new AuthenticatedHttpService(http, auth);

        var authToken = "some auth value";
        var request = new HttpRequest(URI.create("http://whatever"), "foo");
        var response = new HttpResponse(203, "response", new HashMap<>());

        when(http.post(any(HttpRequest.class))).thenReturn(CompletableFuture.completedFuture(response));
        when(auth.getAuthToken()).thenReturn(CompletableFuture.completedFuture(authToken));

        sut.post(request).join();
        sut.post(request).join();

        verify(auth, times(1)).getAuthToken();
    }

    @Test
    void logsInAgainWhenApiReturnsUnauthorized() {
        var http = mock(HttpService.class);
        var auth = mock(AuthService.class);
        var sut = new AuthenticatedHttpService(http, auth);

        var authToken = "some auth value";
        var authToken2 = "some other value";
        var request = new HttpRequest(URI.create("http://whatever"), "foo");
        var unauthorizedResponse = new HttpResponse(401, null, new HashMap<>());
        var response = new HttpResponse(203, "response", new HashMap<>());

        when(http.post(any(HttpRequest.class))).thenReturn(
                CompletableFuture.completedFuture(unauthorizedResponse),
                CompletableFuture.completedFuture(response)
        );
        when(auth.getAuthToken()).thenReturn(
                CompletableFuture.completedFuture(authToken),
                CompletableFuture.completedFuture(authToken2)
        );

        var actualResponse = sut.post(request).join();
        assertEquals(response, actualResponse);

        var arguments = ArgumentCaptor.forClass(HttpRequest.class);
        verify(http, times(2)).post(arguments.capture());

        var actualRequests = arguments.getAllValues();
        assertArrayEquals(new String[] {"Bearer some auth value"}, actualRequests.get(0).headers().get("Authorization").toArray());
        assertArrayEquals(new String[] {"Bearer some other value"}, actualRequests.get(1).headers().get("Authorization").toArray());
    }

    @Test
    void returnsOriginalResponseWhenLoginFails() {
        var http = mock(HttpService.class);
        var auth = mock(AuthService.class);
        var sut = new AuthenticatedHttpService(http, auth);

        var authToken = "some auth value";
        var request = new HttpRequest(URI.create("http://whatever"), "foo");
        var unauthorizedResponse = new HttpResponse(401, null, new HashMap<>());

        when(http.post(any(HttpRequest.class))).thenReturn(
                CompletableFuture.completedFuture(unauthorizedResponse)
        );
        when(auth.getAuthToken()).thenReturn(CompletableFuture.completedFuture(authToken));

        var actualResponse = sut.post(request).join();
        assertEquals(unauthorizedResponse, actualResponse);
    }
}
