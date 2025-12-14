package com.app.ragchatapp.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(2)
public class RateLimiterConfig implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final long capacity;
    private final long refillTokens;
    private final long refillSeconds;

    public RateLimiterConfig(
            @Value("${ragchat.rate-limit.capacity}") long capacity,
            @Value("${ragchat.rate-limit.refill-tokens}") long refillTokens,
            @Value("${ragchat.rate-limit.refill-seconds}") long refillSeconds) {

        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillSeconds = refillSeconds;
    }

    private Bucket createNewBucket() {
        Refill refill = Refill.greedy(refillTokens, Duration.ofSeconds(refillSeconds));

        Bandwidth limit = Bandwidth.classic(capacity, refill);

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket resolveBucket(String clientKey) {
        return buckets.computeIfAbsent(clientKey, key -> createNewBucket());
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // identify the client: prefer API key, fallback to IP
        String apiKey = http.getHeader("X-API-KEY");
        String clientKey = (apiKey != null && !apiKey.isBlank())
                ? apiKey
                : http.getRemoteAddr();

        Bucket bucket = resolveBucket(clientKey);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            res.setStatus(429);
            res.getWriter().write("Too Many Requests");
        }
    }
}