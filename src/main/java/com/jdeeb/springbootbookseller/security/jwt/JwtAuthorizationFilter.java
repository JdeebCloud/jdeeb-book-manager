package com.jdeeb.springbootbookseller.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private IJwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("JwtAuthorizationFilter:shouldNotFilter: started for {}, {}, {} ", request.getMethod(), request.getRequestURI(), request.getRequestURI().startsWith("/api/internal"));
        return request.getRequestURI().startsWith("/api/internal");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtAuthorizationFilter::doFilterInternal started ");
        Authentication authentication = jwtProvider.getAuthentication(request);

        if(authentication != null && jwtProvider.validateToken(request)){
            log.info("JwtAuthorizationFilter::doFilterInternal authenticated: {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("JwtAuthorizationFilter::doFilterInternal filterChain.doFilter finished");

        filterChain.doFilter(request, response);
    }
}
