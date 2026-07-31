package com.g9team10.backend.core.security;

public class PublicRoutes {
    public static String[] ENDPOINTS = {
            "/auth/login",
            "/auth/register",
            "/tags",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
