package com.example.security;

import org.springframework.security.core.AuthenticatedPrincipal;

public class TokenAuthenticatedPrincipal implements AuthenticatedPrincipal {

    private String token;

    public TokenAuthenticatedPrincipal(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return token;
    }
}