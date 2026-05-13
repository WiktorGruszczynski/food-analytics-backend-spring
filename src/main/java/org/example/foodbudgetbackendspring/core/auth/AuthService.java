package org.example.foodbudgetbackendspring.core.auth;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.core.auth.dto.*;
import org.example.foodbudgetbackendspring.core.auth.model.TokenType;
import org.example.foodbudgetbackendspring.core.auth.model.VerificationToken;
import org.example.foodbudgetbackendspring.common.dto.SimpleMessageResponse;
import org.example.foodbudgetbackendspring.core.auth.exception.EmailAlreadyTakenException;
import org.example.foodbudgetbackendspring.core.auth.exception.InvalidVerificationToken;
import org.example.foodbudgetbackendspring.common.MailService;
import org.example.foodbudgetbackendspring.common.util.CodeGenerator;
import org.example.foodbudgetbackendspring.security.JwtService;
import org.example.foodbudgetbackendspring.core.user.model.Role;
import org.example.foodbudgetbackendspring.core.user.model.User;
import org.example.foodbudgetbackendspring.core.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service handling authentication, registration, and account recovery processes.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MailService mailService;
    private final VerificationTokenRepository verificationTokenRepository;

    /**
     * Generates and persists a new verification token (OTP) for the user.
     * Removes all existing tokens belonging to this user before saving the new one.
     */
    @Transactional
    public VerificationToken createVerificationToken(User user, TokenType type) {
        verificationTokenRepository.deleteByUserAndType(user, type);
        verificationTokenRepository.flush();

        VerificationToken token = new VerificationToken(
                CodeGenerator.generateOptCode(),
                type,
                user,
                15
        );
        return verificationTokenRepository.save(token);
    }

    /**
     * Registers a new user or updates the credentials of an unverified account.
     * Sends an activation email with a registration code.
     * @throws EmailAlreadyTakenException if the email is already linked to an active account.
     */
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

    /**
     * Activates a user account based on the provided registration code.
     * @throws InvalidVerificationToken if the code is invalid or has expired.
     */
    @Transactional
    public SimpleMessageResponse verifyAccount(VerificationRequest request){
        VerificationToken token = verificationTokenRepository.findByTokenAndUser_EmailAndType(
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

        verificationTokenRepository.deleteByUserAndType(user, TokenType.REGISTRATION);

        return new SimpleMessageResponse("Account activated successfully. You can now login.");
    }

    /**
     * Primary login step. Verifies credentials and determines whether to issue a JWT
     * or initiate a 2FA (Two-Factor Authentication) procedure.
     */
    @Transactional
    public LoginResponse login(AuthRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );


        User user = (User) auth.getPrincipal();

        if (user == null) {
            throw new BadCredentialsException("Bad credentials");
        }

        if (user.isRequires2FA()) {
            mailService.sendHtmlEmail(
                    request.email(),
                    "2FA Login code",
                    createVerificationToken(user, TokenType.TWO_FACTOR_AUTHENTICATION).getToken()
            );

            return LoginResponse.requires2FA("2FA email sent");
        }

        return LoginResponse.success(
                jwtService.generateJWT(auth)
        );
    }

    /**
     * Secondary login step (2FA). Verifies the email code and issues the final JWT token.
     * @throws InvalidVerificationToken if the 2FA code is invalid or has expired.
     */
    @Transactional
    public TokenResponse verify2FACodeAndLogin(VerificationRequest request){
        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndUser_EmailAndType(
                request.code(),
                request.email(),
                TokenType.TWO_FACTOR_AUTHENTICATION
        ).orElseThrow(
                () -> new InvalidVerificationToken("Invalid verification token")
        );

        if (verificationToken.isExpired()){
            throw new InvalidVerificationToken("Verification token has expired");
        }

        User user = verificationToken.getUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        verificationTokenRepository.delete(verificationToken);

        return new TokenResponse(
                jwtService.generateJWT(auth)
        );
    }

    /**
     * Initiates the password reset procedure. Operates silently (does not throw exceptions
     * if the user is not found) to prevent email enumeration attacks.
     */
    @Transactional
    public void requestPasswordReset(PasswordResetRequest request){
        userRepository.findByEmail(request.email())
                .filter(User::isEnabled)
                .ifPresent(user -> {
                    VerificationToken token = createVerificationToken(
                            user,
                            TokenType.PASSWORD_RESET
                    );

                    mailService.sendHtmlEmail(
                            request.email(),
                            "Password reset",
                            token.getToken()
                    );
                });
    }

    /**
     * Finalizes the password reset. Verifies the token and sets a new encoded password.
     * Utilizes Dirty Checking to automatically update the database record.
     */
    @Transactional
    public void confirmPasswordReset(ConfirmPasswordResetRequest request){
        VerificationToken token = verificationTokenRepository.findByTokenAndUser_EmailAndType(
                request.code(),
                request.email(),
                TokenType.PASSWORD_RESET
        ).orElseThrow(
                () -> new InvalidVerificationToken("Invalid verification token")
        );

        if (token.isExpired()){
            throw new InvalidVerificationToken("Verification token has expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        verificationTokenRepository.delete(token);
    }
}
