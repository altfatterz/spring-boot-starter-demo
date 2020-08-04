package com.example.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class CustomPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    public CustomPreAuthenticatedProcessingFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (!StringUtils.isEmpty(token)) {
            return new TokenAuthenticatedPrincipal(token);
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}

