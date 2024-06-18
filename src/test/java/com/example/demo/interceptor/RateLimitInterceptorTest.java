package com.example.demo.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RateLimitInterceptorTest {

    @InjectMocks
    private RateLimitInterceptor rateLimitInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private static final String API_KEY = "api_key_1";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPreHandleWithInvalidApiKey() throws Exception {
        when(request.getHeader("api_key")).thenReturn(null);

        assertFalse(rateLimitInterceptor.preHandle(request, response, null));
        verify(response).setContentType("application/json; charset=utf-8");
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
    }

    @Test
    public void testPreHandleWithValidApiKeyButExceededRateLimit() throws Exception {
        when(request.getHeader("api_key")).thenReturn(API_KEY);
         RateLimitInterceptor.getApiUsage().put(API_KEY, 6); // exceeds MAX_REQUESTS_PER_HOUR


        assertFalse(rateLimitInterceptor.preHandle(request, response, null));
        verify(response).sendError(429, "Rate limit exceeded");
        verify(response).setContentType("application/json; charset=utf-8");
    }

    @Test
    public void testPreHandleWithValidApiKeyAndWithinRateLimit() throws Exception {
        when(request.getHeader("api_key")).thenReturn(API_KEY);
        RateLimitInterceptor.getApiUsage().put(API_KEY, 3); // within MAX_REQUESTS_PER_HOUR

        assertTrue(rateLimitInterceptor.preHandle(request, response, null));
        assertEquals(4, RateLimitInterceptor.getApiUsage().get(API_KEY));
    }
}
