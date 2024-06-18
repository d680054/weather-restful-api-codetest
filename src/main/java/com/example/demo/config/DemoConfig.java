package com.example.demo.config;

import com.example.demo.interceptor.RateLimitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DemoConfig implements WebMvcConfigurer {

    public static final String API_KEY_1 = "api_key_1";
    public static final String API_KEY_2 = "api_key_2";
    public static final String API_KEY_3 = "api_key_3";
    public static final String API_KEY_4 = "api_key_4";
    public static final String API_KEY_5 = "api_key_5";

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor()).addPathPatterns("/weather/**");
    }
}
