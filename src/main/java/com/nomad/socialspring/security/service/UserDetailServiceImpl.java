package com.nomad.socialspring.security.service;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.security.model.UserDetailsImpl;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(BxException.notFound(User.class, username).getMessage()));

        if (!user.getIsVerified())
            throw BxException.unauthorized(BxException.X_ACCOUNT_NOT_VERIFIED);

        return UserDetailsImpl.build(user);
    }
}
