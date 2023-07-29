package com.jdeeb.springbootbookseller.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Set;

@Slf4j
public class SecurityUtil {
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTH_HEADER = "authorization";
    public static final String AUTH_TOKEN_TYPE = "Bearer";
    public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";

    public static SimpleGrantedAuthority convertToAuthority(String role)
    {
        log.info("SecurityUtil::ConvertToAuthority for role {} started", role);
        String formatterRole = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
        log.info("SecurityUtil::ConvertToAuthority for role {} started to become {}", role, formatterRole);
        return new SimpleGrantedAuthority(formatterRole);
    }

    public static String extractAuthTokenFromRequest(HttpServletRequest request)
    {
        log.info("SecurityUtil:extractAuthTokenFromRequest started");
        String bearerToken = request.getHeader(AUTH_HEADER);
        log.info("SecurityUtil::extractAuthTokenFromRequest bearerToken is {}", bearerToken);
        if(StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_TYPE))
        {
            return bearerToken.substring(7);
        }
        return null;
    }
}
