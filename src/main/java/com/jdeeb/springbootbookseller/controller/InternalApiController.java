package com.jdeeb.springbootbookseller.controller;

import com.jdeeb.springbootbookseller.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/internal/")
@Slf4j
public class InternalApiController {

    @Autowired
    private IUserService userService;

    @PutMapping("make-admin/{username}")    //api/internal/make-admin/{username}
    public ResponseEntity<?> makeAdmin(@PathVariable String username){
        log.info("InternalApiController:makeAdmin started for {} " , username);
        userService.makeAdmin(username);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
