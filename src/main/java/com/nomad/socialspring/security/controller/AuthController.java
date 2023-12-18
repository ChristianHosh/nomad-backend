package com.nomad.socialspring.security.controller;

import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.security.dto.ResendEmailVerificationRequest;
import com.nomad.socialspring.security.service.AuthService;
import com.nomad.socialspring.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Object registerNewUser(@RequestBody @Valid RegisterRequest registerRequest) {
        return authService.registerNewUser(registerRequest);
    }

    @PostMapping("/resend-verification")
    @ResponseBody
    @ResponseStatus(HttpStatus.PROCESSING)
    public UserResponse resendVerificationToken(@RequestBody @Valid ResendEmailVerificationRequest verificationRequest) {
        return authService.resendEmailVerification(verificationRequest);
    }

    @GetMapping("/verify-email")
    @ResponseBody
    @ResponseStatus(HttpStatus.TEMPORARY_REDIRECT)
    public ResponseEntity<?> verifyEmail(@RequestParam(name = "token") String token) {
        return authService.verifyEmail(token);
    }
}
