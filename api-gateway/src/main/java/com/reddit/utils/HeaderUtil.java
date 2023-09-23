package com.reddit.utils;

import org.springframework.cloud.gateway.filter.factory.SecureHeadersProperties;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HeaderUtil {
    private HeaderUtil() {
        throw new IllegalArgumentException("HeaderUtil is a utility class");
    }

    private static MultiValueMap<String, String> securityHeaders = new LinkedMultiValueMap<>();

    public static MultiValueMap<String, String> getSecurityHeaders() {
        if (securityHeaders.isEmpty()) {
            securityHeaders.add("X-XSS-Protection", SecureHeadersProperties.X_XSS_PROTECTION_HEADER_DEFAULT);
            securityHeaders.add("Strict-Transport-Security", SecureHeadersProperties.STRICT_TRANSPORT_SECURITY_HEADER_DEFAULT);
            securityHeaders.add("X-Frame-Options", SecureHeadersProperties.X_FRAME_OPTIONS_HEADER_DEFAULT);
            securityHeaders.add("X-Content-Type-Options", SecureHeadersProperties.X_CONTENT_TYPE_OPTIONS_HEADER_DEFAULT);
            securityHeaders.add("Referrer-Policy", SecureHeadersProperties.REFERRER_POLICY_HEADER_DEFAULT);
            securityHeaders.add("Content-Security-Policy", SecureHeadersProperties.CONTENT_SECURITY_POLICY_HEADER_DEFAULT);
            securityHeaders.add("X-Download-Options", SecureHeadersProperties.X_DOWNLOAD_OPTIONS_HEADER_DEFAULT);
            securityHeaders.add("X-Permitted-Cross-Domain-Policies", SecureHeadersProperties.X_PERMITTED_CROSS_DOMAIN_POLICIES_HEADER_DEFAULT);
        }
        return securityHeaders;
    }
}
