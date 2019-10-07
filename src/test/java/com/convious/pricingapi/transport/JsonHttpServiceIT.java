package com.convious.pricingapi.transport;

import com.convious.pricingapi.driver.MockHttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JsonHttpServiceIT {
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

    private static void assertMultiMapEquals(Map<String, List<String>> expected, Map<String, List<String>> actual) {
        assertEquals(expected.size(), actual.size());
        for (var entry : expected.entrySet()) {
            var actualValue = actual.get(entry.getKey());
            assertNotNull(actualValue);
            assertArrayEquals(entry.getValue().toArray(), actualValue.toArray());
        }
    }

    @Test
    void postShouldMakeHttpRequest() {
        var sut = new JsonHttpService();
        var headers = new HashMap<String, List<String>>() {{
            put("abc", Arrays.asList("123", "456"));
            put("def", Arrays.asList("ghi"));
        }};
        sut.post(new HttpRequest(
                URI.create("http://localhost:" + server.port() + "/some-path?foo=bar&foo=baz&qux=1"),
                "foo",
                headers
        )).join();

        var requests = server.requests();
        assertEquals(1, requests.length);
        var request = requests[0];
        assertEquals("POST", request.method());
        assertEquals("/some-path", request.url());
        assertEquals("\"foo\"", request.body());

        var actualHeaders = new HashMap<>(request.headers());
        actualHeaders.remove("content-length");
        actualHeaders.remove("host");
        actualHeaders.remove("user-agent");
        assertMultiMapEquals(headers, actualHeaders);

        var expectedQuery = new HashMap<String, List<String>>() {{
            put("foo", new LinkedList<>(Arrays.asList("bar", "baz")));
            put("qux", new LinkedList<>(Arrays.asList("1")));
        }};
        assertMultiMapEquals(expectedQuery, request.query());
    }

    @Test
    void postShouldReturnReceivedResponse() {
        var headers = new HashMap<String, List<String>>() {{
            put("abc", Arrays.asList("123", "456"));
            put("def", Arrays.asList("ghi"));
        }};
        server.replyWith(201, "something returned", headers);
        var sut = new JsonHttpService();
        var response = sut.post(new HttpRequest(
                URI.create("http://localhost:" + server.port() + "/some-path?foo=bar&foo=baz&qux=1"),
                "foo"
        )).join();

        assertEquals(201, response.status());
        assertEquals("something returned", response.body());
        var actualHeaders = new HashMap<>(response.headers());
        actualHeaders.remove("content-length");
        actualHeaders.remove("date");
        actualHeaders.remove("connection");
        assertEquals(headers, actualHeaders);
    }
}
