package com.nomad.socialspring.security;

import com.nomad.socialspring.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse registerNewUser(@RequestBody @Valid RegisterRequest registerRequest) {
    return authService.registerNewUser(registerRequest);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse loginUser(@RequestBody @Valid LoginRequest loginRequest) {
    return authService.loginUser(loginRequest);
  }

  @PostMapping("/resend-verification")
  @ResponseStatus(HttpStatus.PROCESSING)
  public UserResponse resendVerificationToken(@RequestBody @Valid ResendEmailVerificationRequest verificationRequest) {
    return authService.resendEmailVerification(verificationRequest);
  }

  @GetMapping("/verify-email")
  @ResponseStatus(HttpStatus.TEMPORARY_REDIRECT)
  public ResponseEntity<?> verifyEmail(@RequestParam(name = "token") String token) {
    return authService.verifyEmail(token);
  }
}
