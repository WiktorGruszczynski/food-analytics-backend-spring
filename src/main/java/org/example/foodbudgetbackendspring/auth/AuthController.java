package org.example.foodbudgetbackendspring.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.auth.dto.AuthRequest;
import org.example.foodbudgetbackendspring.common.dto.TokenResponse;
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
    public ResponseEntity<?> register(
            @Valid @RequestBody AuthRequest request
    ) {
        authService.registerUser(request);
        return ResponseEntity.ok("success");
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
