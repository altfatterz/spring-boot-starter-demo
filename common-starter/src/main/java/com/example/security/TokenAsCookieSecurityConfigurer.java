package com.example.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TokenAsCookieSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthenticationManager authenticationManager;

    public TokenAsCookieSecurityConfigurer(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void configure(HttpSecurity httpSecurity) {
        TokenAsCookiePreAuthenticatedProcessingFilter filter = new TokenAsCookiePreAuthenticatedProcessingFilter(authenticationManager);
        filter.setAuthenticationDetailsSource(context -> (GrantedAuthoritiesContainer) () -> List.of(new SimpleGrantedAuthority("ROLE_USER")));
        httpSecurity.addFilterAt(filter, AbstractPreAuthenticatedProcessingFilter.class);
    }

    static class TokenAsCookiePreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

        public TokenAsCookiePreAuthenticatedProcessingFilter(AuthenticationManager authenticationManager) {
            super.setAuthenticationManager(authenticationManager);
        }

        @Override
        protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("token")) {
                        return new TokenAuthenticatedPrincipal(cookie.getValue());
                    }
                }
            }
            return null;
        }

        @Override
        protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
            return "N/A";
        }
    }
}
