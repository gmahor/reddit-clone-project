package com.reddit.config;

import com.reddit.filters.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;

    public GatewayConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service-route", r -> r.path("/api/auth/**",
                                "/api/Subreddit/**",
                                "/api/post/**",
                                "/api/comment/**",
                                "/api/votes/**"
                        )
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://user-service"))
                .build();
    }
}
