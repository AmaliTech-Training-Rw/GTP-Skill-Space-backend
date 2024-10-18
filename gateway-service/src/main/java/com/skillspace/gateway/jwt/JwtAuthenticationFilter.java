package com.skillspace.gateway.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtValidator jwtValidator;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    // Core filter method to apply the filter logic when a request is received.
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = extractToken(request);

            // Validate the token
            if (!validateToken(exchange, token)) {
                return onError(exchange, "Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            // Extract email and role from the token
            String email = jwtValidator.getEmailFromToken(token);
            String role = jwtValidator.getRoleFromToken(token);

            // Check user access to the requested URL path
            if (!isAuthorized(request.getURI().getPath(), role, exchange)) {
                return onError(exchange, "Forbidden", HttpStatus.FORBIDDEN);
            }

            // Forward the request with user details if authorized
            ServerHttpRequest modifiedRequest = addHeaders(request, email, role);
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    // Helper method to validate the token and handle unauthorized responses
    private boolean validateToken(ServerWebExchange exchange, String token) {
        if (token == null) {
            onError(exchange, "No JWT token found in request headers", HttpStatus.UNAUTHORIZED).subscribe();
            return false;
        }
        if (!jwtValidator.validateToken(token)) {
            onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED).subscribe();
            return false;
        }
        return true;
    }

    // Helper method for authorization logic
    private boolean isAuthorized(String path, String role, ServerWebExchange exchange) {
        if (isAdminPath(path) && !"ADMIN".equals(role)) {
            onError(exchange, "Forbidden: Admins only", HttpStatus.FORBIDDEN).subscribe();
            return false;
        }
        if (isTalentPath(path) && !"TALENT".equals(role)) {
            onError(exchange, "Forbidden: Talents only", HttpStatus.FORBIDDEN).subscribe();
            return false;
        }
        if (isCompanyPath(path) && !"COMPANY".equals(role)) {
            onError(exchange, "Forbidden: Companies only", HttpStatus.FORBIDDEN).subscribe();
            return false;
        }
        if (isCareerServicePath(path)) {
            String method = exchange.getRequest().getMethod().name();
            if ((method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) && !"COMPANY".equals(role)) {
                onError(exchange, "Forbidden: Admins only for create, update, and delete operations", HttpStatus.FORBIDDEN).subscribe();
                return false;
            }
        }
        return true;
    }

    // Helper method to add headers to the request
    private ServerHttpRequest addHeaders(ServerHttpRequest request, String email, String role) {
        return request.mutate()
                .header("X-Auth-User", email)
                .header("X-User-Role", role)
                .build();
    }

    // Helper method to extract the JWT from the Authorization header of the HTTP request
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Method to handle errors
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    // Path checking methods
    private boolean isAdminPath(String path) {
        return path.startsWith("/admin");
    }

    private boolean isTalentPath(String path) {
        return path.startsWith("/talent");
    }

    private boolean isCompanyPath(String path) {
        return path.startsWith("/company");
    }

    private boolean isCareerServicePath(String path) {
        return path.startsWith("/careers");
    }

    public static class Config {
        // Configuration properties
    }
}