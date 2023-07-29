package com.jdeeb.springbootbookseller.security;

import com.jdeeb.springbootbookseller.model.User;
import com.jdeeb.springbootbookseller.service.IUserService;
import com.jdeeb.springbootbookseller.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    @Lazy
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        log.info("CustomUserDetailsService:loadUserByUsername");
        User user = userService.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException(username));

        log.info("User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + user.getUsername());

        Set<GrantedAuthority> authorities = Set.of(SecurityUtil.convertToAuthority(user.getRole().name()));
        return UserPrincipal.builder()
                .user(user).id(user.getId())
                .username(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
