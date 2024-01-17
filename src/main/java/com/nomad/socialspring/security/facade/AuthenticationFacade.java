package com.nomad.socialspring.security.facade;

import com.nomad.socialspring.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface AuthenticationFacade {
    static BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    Authentication getAuthentication();

    User getAuthenticatedUser();

    User getAuthenticatedUserOrNull();
}