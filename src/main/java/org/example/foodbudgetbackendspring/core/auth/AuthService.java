package org.example.foodbudgetbackendspring.core.auth;

import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.core.auth.dto.AuthRequest;
import org.example.foodbudgetbackendspring.core.auth.dto.TokenResponse;
import org.example.foodbudgetbackendspring.core.auth.dto.VerificationRequest;
import org.example.foodbudgetbackendspring.core.auth.model.TokenType;
import org.example.foodbudgetbackendspring.core.auth.model.VerificationToken;
import org.example.foodbudgetbackendspring.common.dto.SimpleMessageResponse;
import org.example.foodbudgetbackendspring.core.auth.exception.EmailAlreadyTakenException;
import org.example.foodbudgetbackendspring.core.auth.exception.InvalidVerificationToken;
import org.example.foodbudgetbackendspring.common.MailService;
import org.example.foodbudgetbackendspring.common.util.CodeGenerator;
import org.example.foodbudgetbackendspring.security.TokenService;
import org.example.foodbudgetbackendspring.core.user.model.Role;
import org.example.foodbudgetbackendspring.core.user.model.User;
import org.example.foodbudgetbackendspring.core.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final MailService mailService;
    private final TokenRepository tokenRepository;

    @Transactional
    public VerificationToken createVerificationToken(User user, TokenType type) {
        tokenRepository.deleteByUser(user);
        tokenRepository.flush();

        VerificationToken token = new VerificationToken(
                CodeGenerator.generateOptCode(),
                type,
                user,
                15
        );
        return tokenRepository.save(token);
    }

    @Transactional
    public SimpleMessageResponse register(AuthRequest request){
        User user = userRepository.findByEmail(request.email())
                .map(existingUser -> {
                    if (existingUser.isEnabled()) {
                        throw new EmailAlreadyTakenException("Email already taken");
                    }

                    existingUser.setPassword(passwordEncoder.encode(request.password()));
                    return existingUser;
                }
        ).orElseGet(() -> User.builder()
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .role(Role.USER)
                        .enabled(false)
                        .build()
                );

        VerificationToken token = createVerificationToken(
                userRepository.save(user),
                TokenType.REGISTRATION
        );

        mailService.sendHtmlEmail(request.email(), "Registration", token.getToken());

        return new SimpleMessageResponse("Successfully registered. Please check your email.");
    }

    @Transactional
    public SimpleMessageResponse verifyAccount(VerificationRequest request){
        VerificationToken token = tokenRepository.findByTokenAndUser_EmailAndType(
                request.code(),
                request.email(),
                TokenType.REGISTRATION
        ).orElseThrow(() -> new InvalidVerificationToken("Invalid verification token"));

        if (token.isExpired()){
            throw new InvalidVerificationToken("Verification token has expired");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.deleteByUser(user);

        return new SimpleMessageResponse("Account activated successfully. You can now login.");
    }

    public TokenResponse login(AuthRequest request){
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            return new TokenResponse(
                    tokenService.generateToken(auth)
            );
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
