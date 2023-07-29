package com.jdeeb.springbootbookseller.security.jwt;

import com.jdeeb.springbootbookseller.security.UserPrincipal;
import com.jdeeb.springbootbookseller.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider implements IJwtProvider
{
    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPRIRATION_IN_MS;

    @Override
    public String generateToken(UserPrincipal auth)
    {
        log.info("JwtProvider::generateToken started");
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        log.info("JwtProvider::generateToken Auth.authorities {}", auth.getAuthorities());
        log.info("JwtProvider::generateToken authorities {}", authorities);
        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPRIRATION_IN_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request)
    {
        log.info("JwtProvider::getAuthentication Started for {}", request.getRequestURI());
        Claims claims = extractClaims(request);
        log.info("JwtProvider::getAuthentication claims are {} ", claims);
        if(claims == null)  return null;

        String username = claims.getSubject();

        Long userId = claims.get("userId", Long.class);
        log.info("JwtProvider::getAuthentication username from claims is {} while userId is {}" ,username, userId);

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtil::convertToAuthority)
                .collect(Collectors.toSet());

        log.info("JwtProvider::getAuthentication authorities {}" ,authorities);

        UserPrincipal userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        log.info("JwtProvider::getAuthentication userDetails {}", userDetails.getAuthorities());
        if(username == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Override
    public boolean validateToken(HttpServletRequest request)
    {
        log.info("JwtProvider::validateToken started");
        Claims claims = extractClaims(request);
        log.info("JwtProvider::validateToken claims are {} ", claims);
        if(claims == null)  return false;

        if(claims.getExpiration().before(new Date())){
            log.info("JwtProvider::validateToken:Expiration is before now");
            return false;
        }
        log.info("JwtProvider::validateToken Token is correct and valid");
        return true;

    }

    private Claims extractClaims(HttpServletRequest request){
        log.info("JwtProvider::getAuthentication started");
        String token = SecurityUtil.extractAuthTokenFromRequest(request);
        log.info("JwtProvider::getAuthentication Token is {}", token);
        if(token == null){
            return null;
        }
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
