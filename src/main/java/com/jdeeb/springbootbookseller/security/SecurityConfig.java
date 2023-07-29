package com.jdeeb.springbootbookseller.security;

import com.jdeeb.springbootbookseller.model.Role;
import com.jdeeb.springbootbookseller.security.jwt.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig
{

    @Value("${authentication.internal-api-key}")
    private String internalApiKey;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        log.info("SecurityConfig:authenticationManager started");
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("SecurityConfig:filterChain started");
        http.httpBasic(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( (authorize) -> authorize
                        .requestMatchers("/api/authentication/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/book").permitAll()
                        .requestMatchers("/api/book/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/internal/**").hasRole(Role.SYSTEM_MANAGER.name())
                        .anyRequest().authenticated()
                );
        // JWT Filters
        // Filters order :  internal > jwt > authentication
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(internalApiAuthenticationFilter(), JwtAuthorizationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigure()
    {
        log.info("SecurityConfig:corsConfigure started");
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() { log.info("SecurityConfig:passwordEncoder() started"); return new BCryptPasswordEncoder(); }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        log.info("SecurityConfig:jwtAuthorizationFilter() started"); return new JwtAuthorizationFilter();
    }
    @Bean
    public InternalApiAuthenticationFilter internalApiAuthenticationFilter(){
        log.info("SecurityConfig:internalApiAuthenticationFilter() started");
        return new InternalApiAuthenticationFilter(internalApiKey);
    }
}
