package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;

public class SecurityConfiguration {

    @Configuration
    @Order(1)
    public static class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private SecurityProblemSupport problemSupport;

        @Autowired
        private Environment environment;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            // @formatter:off
            http
                    .antMatcher("/api/**")
                    .csrf()
                        .disable()
                    .authorizeRequests()
                        .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(problemSupport)
                        .accessDeniedHandler(problemSupport);
            // @formatter:on

            if (Arrays.asList(environment.getActiveProfiles()).contains("swagger")) {
                http.apply(new TokenAsQueryParameterSecurityConfigurer(authenticationManager()));
            } else {
                http.apply(new TokenAsCookieSecurityConfigurer(authenticationManager()));
            }
        }

        @Bean
        public PreAuthenticatedAuthenticationProvider authenticationProvider() {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedGrantedAuthoritiesUserDetailsService());
            return provider;
        }
    }


    @Configuration
    @Order(2)
    public static class ActuatorSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private SecurityProperties securityProperties;

        public ActuatorSecurityConfiguration(SecurityProperties securityProperties) {
            this.securityProperties = securityProperties;
        }

        protected void configure(HttpSecurity http) throws Exception {

            // @formatter:off
            http
                    .antMatcher("/actuator/**")
                    .csrf()
                        .disable()
                    .authorizeRequests()
                        .antMatchers("/actuator/health/**").permitAll()
                        .antMatchers("/actuator/info").permitAll()
                        .antMatchers("/actuator/**").hasRole("ADMIN")
                    .and()
                    .httpBasic()
            ;
            // @formatter:on

        }

        @Override
        public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder.inMemoryAuthentication().withUser(securityProperties.getUser().getName())
                    .password("{noop}" + securityProperties.getUser().getPassword()).roles("ADMIN");

        }
    }

}