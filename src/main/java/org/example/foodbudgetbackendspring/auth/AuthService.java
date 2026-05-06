package org.example.foodbudgetbackendspring.auth;

import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.auth.dto.AuthRequest;
import org.example.foodbudgetbackendspring.common.dto.TokenResponse;
import org.example.foodbudgetbackendspring.common.exception.EmailAlreadyTakenException;
import org.example.foodbudgetbackendspring.security.TokenService;
import org.example.foodbudgetbackendspring.user.model.Role;
import org.example.foodbudgetbackendspring.user.model.User;
import org.example.foodbudgetbackendspring.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public void registerUser(AuthRequest request){
        if (userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyTakenException("Email already in use");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public TokenResponse login(AuthRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        return new TokenResponse(
                tokenService.generateToken(auth)
        );
    }
}
