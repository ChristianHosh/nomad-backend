package com.nomad.socialspring.security.facade;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.repo.UserRepository;
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
                .orElseThrow(() -> BxException.notFound(User.class, username));
    }

    @Override
    public User getAuthenticatedUserOrNull() {
        return userRepository.findByUsername(getAuthentication().getName())
                .orElse(null);
    }
}
