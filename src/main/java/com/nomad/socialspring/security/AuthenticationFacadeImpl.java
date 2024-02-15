package com.nomad.socialspring.security;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserRepository userRepository;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getAuthenticatedUser() {
        String username = getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> BxException.unauthorized(BxException.X_NOT_LOGGED_IN));
    }

    @Override
    public User getAuthenticatedUserOrNull() {
        return userRepository.findByUsername(getAuthentication().getName())
                .orElse(null);
    }
}
