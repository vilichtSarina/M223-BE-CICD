package com.example.demo.core.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
        log.warn("Error, unable to find user.");
    }
}
