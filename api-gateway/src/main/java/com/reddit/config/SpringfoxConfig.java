package com.reddit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.stream.Collectors;

@Configuration
public class SpringfoxConfig {

    @Value("${spring.application.name}")
    private String gateway;

    @Bean
    @Primary
    public SwaggerResourcesProvider swaggerResourcesProvider(DiscoveryClient discoveryClient) {
        return () -> discoveryClient.getServices()
                .stream()
                .filter(service -> !service.equals(gateway))
                .map(service -> {
                    SwaggerResource resource = new SwaggerResource();
                    resource.setName(service);
                    resource.setLocation(String.format("/%s/v3/api-docs", service));
                    return resource;
                })
                .collect(Collectors.toList());
    }
}
