package org.example.foodbudgetbackendspring.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.mail.MailService;
import org.example.foodbudgetbackendspring.user.dto.RegisterRequest;
import org.example.foodbudgetbackendspring.user.model.User;
import org.example.foodbudgetbackendspring.user.model.VerificationCode;
import org.example.foodbudgetbackendspring.user.repository.UserRepository;
import org.example.foodbudgetbackendspring.user.repository.VerificationCodeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    private String generateVerificationCode(){
        return String.valueOf(
                new SecureRandom().nextInt(900_000) + 100_000
        );
    }

    @Transactional
    public void register(RegisterRequest request){
        User user = userRepository.findByEmail(request.email()).orElse(null);

        if (user != null) {
            if (user.isEnabled()){
                throw new RuntimeException("User is registered and enabled");
            }
            else {
                throw new RuntimeException("User registered, waiting for verification code");
            }
        }

        user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(generateVerificationCode());
        verificationCode.setUser(user);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationCodeRepository.save(verificationCode);

        mailService.sendRegistrationEmail(
                user.getEmail(),
                verificationCode.getCode()
        );
    }

    @Transactional
    public void verifyRegistration(String code){
        VerificationCode verificationCode = verificationCodeRepository
                .findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid verification code"));

        if (verificationCode.isExpired()){
            throw new RuntimeException("Expired verification code");
        }

        User user = verificationCode.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationCodeRepository.delete(verificationCode);
    }

    @Transactional
    public void resendVerificationCode(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
            throw new RuntimeException("User is already verified");
        }

        VerificationCode verificationCode = verificationCodeRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Invalid verification code"));

        verificationCode.setCode(generateVerificationCode());
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationCodeRepository.save(verificationCode);

        mailService.sendRegistrationEmail(
                user.getEmail(),
                verificationCode.getCode()
        );
    }
}
