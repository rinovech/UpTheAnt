package com.uptheant.demo.service.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {

            log.debug("Request URL: {}", request.getRequestURL());
            log.debug("Request Method: {}", request.getMethod());
            log.debug("Authorization Header: {}", request.getHeader("Authorization"));
            
            String authHeader = request.getHeader(HEADER_NAME);
            String jwt = null;
            String username = null;
            
            if (StringUtils.isNotEmpty(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
                jwt = authHeader.substring(BEARER_PREFIX.length());
                username = jwtUtils.getUserNameFromJwtToken(jwt);
                log.debug("JWT Token found: {}", jwt);
                log.debug("Username from token: {}", username);
            }
            
            if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                if (jwtUtils.validateJwtToken(jwt)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                    log.debug("User authenticated: {}", username);
                }
            }
            
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Authentication Filter Error", e);
            throw e;
        }
    }
}