package com.nomad.socialspring.security.service;

import com.nomad.socialspring.error.exceptions.BException;
import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.security.mapper.VerificationTokenMapper;
import com.nomad.socialspring.security.model.VerificationToken;
import com.nomad.socialspring.security.repo.VerificationTokenRepository;
import com.nomad.socialspring.user.mapper.UserMapper;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;


    @Transactional
    public Object registerNewUser(@NotNull RegisterRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.username()))
            throw BException.conflict(User.class, "username", request.username());
        if (userRepository.existsByEmailIgnoreCase(request.email()))
            throw BException.conflict(User.class, "email", request.email());

        User user = userRepository
                .save(UserMapper.requestToEntity(request));
        VerificationToken verificationToken = verificationTokenRepository
                .save(VerificationTokenMapper.accountToken(user));

        mailService.sendVerificationEmail(user, verificationToken);

        return null;
    }

    @Transactional
    public ResponseEntity<?> verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> BException.notFound(VerificationToken.class, token));

        User user = verificationToken.getUser();
        user.setIsVerified(true);

        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create("https://localhost:5300"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
