package com.nomad.socialspring.user.repo;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.user.mapper.UserMapper;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository repository;

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(BxException.xNotFound(User.class, id));
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(BxException.xNotFound(User.class, username));
    }

    public User findByEmailIgnoreCase(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(BxException.xNotFound(User.class, email));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User save(RegisterRequest registerRequest) {
        return repository.save(UserMapper.requestToEntity(registerRequest));
    }

    public boolean existsByUsernameIgnoreCase(String username) {
        return repository.existsByUsernameIgnoreCase(username);
    }

    public boolean existsByEmailIgnoreCase(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    public void verify(@NotNull User user) {
        user.setIsVerified(true);
        save(user);
    }
}
