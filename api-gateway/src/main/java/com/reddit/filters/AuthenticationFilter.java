package com.reddit.filters;

import com.reddit.config.RouterValidator;
import com.reddit.utils.HeaderUtil;
import com.reddit.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationFilter(RouterValidator routerValidator,
                                JwtUtil jwtUtil) {
        this.routerValidator = routerValidator;
        this.jwtUtil = jwtUtil;

    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return exchange.getResponse().setComplete();
        }
        if (routerValidator.isSecured().test(request)) {
            if (this.isAuthMissing(request)) {
                log.info("Authorization header is missing in request");
                return this.onError(exchange);
            }
            final String token = this.getAuthHeader(request);
            final Claims claims = jwtUtil.getAllClaimsFromToken(token);
            if (!jwtUtil.validateToken(claims)) {
                log.info("Invalid token");
                return this.onError(exchange);
            }
            this.populateRequestWithHeaders(exchange, claims);
        }
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().addAll(HeaderUtil.getSecurityHeaders());
        return chain.filter(exchange);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, Claims claims) {
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("roles", String.valueOf(claims.get("roles")))
                .header("Accept-Language", String.valueOf(claims.get("lang")))
                .build();
    }
}