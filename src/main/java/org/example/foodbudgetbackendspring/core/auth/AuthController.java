package org.example.foodbudgetbackendspring.core.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.core.auth.dto.*;
import org.example.foodbudgetbackendspring.common.dto.SimpleMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SimpleMessageResponse> register(
            @Valid @RequestBody AuthRequest request
    ){
        return ResponseEntity.ok(
                authService.register(request)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<SimpleMessageResponse> verifyAccount(
            @Valid @RequestBody VerificationRequest request
    ){
        return ResponseEntity.ok(
                authService.verifyAccount(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody AuthRequest request
    ){
        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/login/verify-2fa")
    public ResponseEntity<TokenResponse> verify2fa(
            @Valid @RequestBody VerificationRequest request
    ){
        return ResponseEntity.ok(
                authService.verify2FACodeAndLogin(request)
        );
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<SimpleMessageResponse> requestForgetPassword(
            @Valid @RequestBody PasswordResetRequest request
    ){
        authService.requestPasswordReset(request);
        return ResponseEntity.ok(
            new SimpleMessageResponse("Email sent successfully!")
        );
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<SimpleMessageResponse> confirmPasswordReset(
            @Valid @RequestBody ConfirmPasswordResetRequest request
    ){
        authService.confirmPasswordReset(request);
        return ResponseEntity.ok(
                new SimpleMessageResponse("Password reset successfully!")
        );
    }
}
