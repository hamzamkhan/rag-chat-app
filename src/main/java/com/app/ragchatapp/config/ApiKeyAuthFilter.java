package com.app.ragchatapp.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class ApiKeyAuthFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyAuthFilter.class);
    private static final String HEADER_NAME = "X-API-KEY";

    private final ApiKeyConfig apiKeyConfig;

    public ApiKeyAuthFilter(ApiKeyConfig apiKeyConfig) {
        this.apiKeyConfig = apiKeyConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = http.getRequestURI();
        if (path.startsWith("/actuator/health") || path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui")) {
            chain.doFilter(request, response);
            return;
        }

        String apiKey = http.getHeader(HEADER_NAME);
        if (apiKey == null || !apiKey.equals(apiKeyConfig.getExpectedApiKey())) {
            log.warn("Unauthorized request to {} from {}", path, request.getRemoteAddr());
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }
}
