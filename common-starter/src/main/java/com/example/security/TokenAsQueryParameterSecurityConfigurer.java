package com.example.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TokenAsQueryParameterSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthenticationManager authenticationManager;

    public TokenAsQueryParameterSecurityConfigurer(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void configure(HttpSecurity httpSecurity) {
        TokenAsQueryParameterPreAuthenticatedProcessingFilter filter = new TokenAsQueryParameterPreAuthenticatedProcessingFilter(authenticationManager);
        filter.setAuthenticationDetailsSource(context -> (GrantedAuthoritiesContainer) () -> List.of(new SimpleGrantedAuthority("ROLE_USER")));
        httpSecurity.addFilterAt(filter, AbstractPreAuthenticatedProcessingFilter.class);
    }

    static class TokenAsQueryParameterPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

        public TokenAsQueryParameterPreAuthenticatedProcessingFilter(AuthenticationManager authenticationManager) {
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
}
