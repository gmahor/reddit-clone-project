package com.reddit.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    private final List<String> openApiEndpoints = Arrays.asList(
            "/api/auth/signup",
            "/api/auth/accountVerification/",
            "/api/auth/login"
    );

    public Predicate<ServerHttpRequest> isSecured() {
        return request -> openApiEndpoints
                .stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));
    }
}
