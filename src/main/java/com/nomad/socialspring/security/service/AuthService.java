package com.nomad.socialspring.security.service;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.security.dto.LoginRequest;
import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.security.dto.ResendEmailVerificationRequest;
import com.nomad.socialspring.security.jwt.JwtUtils;
import com.nomad.socialspring.security.model.VerificationToken;
import com.nomad.socialspring.user.dto.UserResponse;
import com.nomad.socialspring.user.model.UserMapper;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.service.UserFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserFacade userFacade;
    private final VerificationTokenFacade verificationTokenFacade;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    @Transactional
    public UserResponse registerNewUser(@NotNull RegisterRequest request) {
        if (userFacade.existsByUsernameIgnoreCase(request.username()))
            throw BxException.conflict(User.class, "username", request.username());
        if (userFacade.existsByEmailIgnoreCase(request.email()))
            throw BxException.conflict(User.class, "email", request.email());

        User user = userFacade.save(request);
        VerificationToken verificationToken = verificationTokenFacade.accountSave(user);

        mailService.sendVerificationEmail(user, verificationToken);

        return UserMapper.entityToResponse(user);
    }

    @Transactional
    public UserResponse resendEmailVerification(@NotNull ResendEmailVerificationRequest request) {
        User user = userFacade.findByEmailIgnoreCase(request.email());
        VerificationToken verificationToken = verificationTokenFacade.findByUser(user);

        if (BDate.valueOf(verificationToken.getExpirationDate()).before(BDate.currentDate())) {
            throw BxException.badRequest(VerificationToken.class, "date", BxException.X_DATE_NOT_EXPIRED);
        }

        verificationTokenFacade.delete(verificationToken);
        verificationToken = verificationTokenFacade.accountSave(user);

        mailService.sendVerificationEmail(user, verificationToken);

        return UserMapper.entityToResponse(user);
    }

    @Transactional
    public ResponseEntity<?> verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenFacade.findByToken(token);

        userFacade.verify(verificationToken.getUser());
        verificationTokenFacade.delete(verificationToken);

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create("https://localhost:5300"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    public UserResponse loginUser(@NotNull LoginRequest request) {
        if (!userFacade.findByUsername(request.username()).getIsVerified())
            throw BxException.unauthorized(BxException.X_ACCOUNT_NOT_VERIFIED);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = userFacade.findByUsername(request.username());

        return UserMapper.entityToResponse(user, jwt);
    }
}
