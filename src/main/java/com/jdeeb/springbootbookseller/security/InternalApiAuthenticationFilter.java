package com.jdeeb.springbootbookseller.security;

import com.jdeeb.springbootbookseller.model.User;
import com.jdeeb.springbootbookseller.util.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class InternalApiAuthenticationFilter extends OncePerRequestFilter {

    private final String accessKey;

    public InternalApiAuthenticationFilter(String accessKey){
        this.accessKey = accessKey;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("InternalAPIAuthenticationFilter:shouldNotFilter: started for {}, {}, {} ", request.getMethod(), request.getRequestURI(), !request.getRequestURI().startsWith("/api/internal"));
        return !request.getRequestURI().startsWith("/api/internal");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("InternalAPIAuthenticationFilter:doFilterInternal: started");
        try{
            String requestKey = SecurityUtil.extractAuthTokenFromRequest(request);
            log.info("InternalAPIAuthenticationFilter:doFilterInternal requestKey: {}", requestKey);
            if(requestKey == null || !requestKey.equals(accessKey)){
                log.warn("Internal Key endpoint requested without/wrong key uri {}", request.getRequestURI());

                throw new RuntimeException("UNAUTHORIZED");
            }

            UserPrincipal user = UserPrincipal.createSuperUser();
            log.info("InternalAPIAuthenticationFilter superuser created: {} ", user);
            UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            log.info("InternalAPIAuthenticationFilter UsernamePasswordAuthenticationToken created: {} ", authenticated);
            SecurityContextHolder.getContext().setAuthentication(authenticated);

        }catch (Exception ex){
            log.error("Could not set user authentication in security context", ex);
        }
        log.info("InternalAPIAuthenticationFilter doFilter called");
        filterChain.doFilter(request, response);
    }
}
