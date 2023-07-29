package com.jdeeb.springbootbookseller.service;

import com.jdeeb.springbootbookseller.model.User;

public interface IAuthenticationService {
    User signInAndReturnJWT(User signInRequest);
}
