package com.nomad.socialspring.security;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.error.BxNotFoundException;
import com.nomad.socialspring.user.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    if (request.username().equalsIgnoreCase("anonymousUser") || userFacade.existsByUsernameIgnoreCase(request.username()))
      throw BxException.conflict(User.class, "username", request.username());
    if (userFacade.existsByEmailIgnoreCase(request.email()))
      throw BxException.conflict(User.class, "email", request.email());

    User user = userFacade.save(request);
    VerificationToken verificationToken = verificationTokenFacade.accountSave(user);

    mailService.sendVerificationEmail(user, verificationToken);

    return loginUser(new LoginRequest(request.username(), request.password()));
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

    return user.toResponse();
  }

  @Transactional
  public ResponseEntity<Object> verifyEmail(String token) {
    VerificationToken verificationToken = verificationTokenFacade.findByToken(token);

    userFacade.verify(verificationToken.getUser());
    verificationTokenFacade.delete(verificationToken);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("http://localhost:5173/login"));

    return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
  }

  public UserResponse loginUser(@NotNull LoginRequest request) {
    User user;
    try {
      user = userFacade.findByUsername(request.username());
    } catch (BxNotFoundException e) {
      throw BxException.unauthorized(BxException.X_BAD_CREDENTIALS);
    }

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    return user.toResponse(null, true).withToken(jwt);
  }

  public UserResponse resetPassword(AuthController.ResetPasswordRequest resetPasswordRequest) {
    User user = userFacade.getCurrentUser();

    PasswordEncoder passwordEncoder = AuthenticationFacade.getEncoder();
    String encodedOldPassword = passwordEncoder.encode(resetPasswordRequest.oldPassword());
    if (user.getPassword().equalsIgnoreCase(encodedOldPassword)) {
      if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmPassword())) {
        throw BxException.badRequest(User.class, "confirmPassword", BxException.X_PASSWORDS_DO_NOT_MATCH);
      }

      user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));

      return user.toResponse();
    } else {
      throw BxException.badRequest(User.class, "password", BxException.X_BAD_CREDENTIALS);
    }
  }
}
