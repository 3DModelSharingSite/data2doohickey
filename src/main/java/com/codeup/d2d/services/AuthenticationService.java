package com.codeup.d2d.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("authenticationService")
public class AuthenticationService {
    public Object getCurUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
