package org.example.foodbudgetbackendspring.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.auth.dto.AuthRequest;
import org.example.foodbudgetbackendspring.auth.dto.TokenResponse;
import org.example.foodbudgetbackendspring.auth.dto.VerificationRequest;
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
    public ResponseEntity<SimpleMessageResponse> verify(
            @Valid @RequestBody VerificationRequest request
    ){
        return ResponseEntity.ok(
                authService.verifyAccount(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody AuthRequest request
    ){
        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}
