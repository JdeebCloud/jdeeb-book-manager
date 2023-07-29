package com.jdeeb.springbootbookseller.service;

import com.jdeeb.springbootbookseller.model.User;
import com.jdeeb.springbootbookseller.security.UserPrincipal;
import com.jdeeb.springbootbookseller.security.jwt.IJwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService implements IAuthenticationService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IJwtProvider jwtProvider;

    @Override
    public User signInAndReturnJWT(User signInRequest)
    {
        log.info("CAll SignInAnd :: " +  signInRequest.getUsername() + "::" + signInRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        log.info(authentication.getName());
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwt = jwtProvider.generateToken(userPrincipal);

        User signInUser = userPrincipal.getUser();

        signInUser.setToken(jwt);

        return signInUser;

    }
}
