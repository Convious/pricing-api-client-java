package com.convious.pricingapi.transport;

import com.convious.pricingapi.driver.MockHttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpAuthServiceIT {
    private MockHttpServer server;

    @BeforeEach
    void startHttpServer() {
        server = new MockHttpServer();
        server.start();
    }

    @AfterEach
    void stopHttpServer() {
        server.stop();
    }

    @Test
    void postsCorrectDataToServer() {
        var sut = new HttpAuthService("http://localhost:" + server.port(), "some_client", "some_secure_string");
        server.replyWith(200, "{\"access_token\":\"some access token\"}");
        sut.getAuthToken().join();

        var expected = "grant_type=client_credentials&client_id=some_client&client_secret=some_secure_string";

        var requests = server.requests();
        assertEquals(1, requests.length);
        var request = requests[0];
        assertEquals(request.method(), "POST");
        assertEquals(request.url(), "/oauth/token/");
        assertEquals(request.body(), expected);
    }

    @Test
    void getsAuthTokenWithOAuthPost() {
        var sut = new HttpAuthService("http://localhost:" + server.port(), "some_client", "some_secure_string");
        server.replyWith(200, "{\"access_token\":\"some access token\"}");
        var actual = sut.getAuthToken().join();

        assertEquals("some access token", actual);
    }

    @Test
    void throwsWhenApiReturnsIncorrectCredentials() {
        var sut = new HttpAuthService("http://localhost:" + server.port(), "some_client", "some_secure_string");
        server.replyWith(400, "{\"access_token\":\"some access token\"}");
        assertThrows(Exception.class, () -> sut.getAuthToken().join());
    }
}
