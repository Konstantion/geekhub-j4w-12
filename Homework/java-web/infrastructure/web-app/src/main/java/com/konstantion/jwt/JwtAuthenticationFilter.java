package com.konstantion.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static java.util.Objects.requireNonNullElse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        String username = requireNonNullElse(obtainUsername(request), "").strip();
        String password = requireNonNullElse(obtainPassword(request), "").strip();

        var authRequest = new UsernamePasswordAuthenticationToken(username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) {
        String token = jwtService.generateToken((UserDetails) authResult.getPrincipal());
        response.addHeader(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PREFIX + token);
    }
}
