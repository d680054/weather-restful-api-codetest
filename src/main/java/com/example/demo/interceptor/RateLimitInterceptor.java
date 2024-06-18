package com.example.demo.interceptor;

import com.example.demo.config.DemoConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private static Map<String, Integer> apiUsage = ExpiringMap.builder()
			.expiration(1, TimeUnit.MINUTES)
			.build();

    private final int MAX_REQUESTS_PER_HOUR = 5;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader("api_key");

        if (apiKey == null || apiKey.isEmpty() || !isValidApiKey(apiKey)) {
            response.setContentType("application/json; charset=utf-8");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            log.warn("Unauthorized Access");

            return false;
        }

        int requests = apiUsage.getOrDefault(apiKey, 0);
        if (requests >= MAX_REQUESTS_PER_HOUR) {
            response.sendError(429, "Rate limit exceeded");
            response.setContentType("application/json; charset=utf-8");
            log.warn("Rate Limit Exceeded");

            return false;
        }

        apiUsage.put(apiKey, requests + 1);
        return true;
    }

    private boolean isValidApiKey(String apiKey) {
        return DemoConfig.API_KEY_1.equals(apiKey) ||
               DemoConfig.API_KEY_2.equals(apiKey) ||
               DemoConfig.API_KEY_3.equals(apiKey) ||
               DemoConfig.API_KEY_4.equals(apiKey) ||
               DemoConfig.API_KEY_5.equals(apiKey);
    }
}
