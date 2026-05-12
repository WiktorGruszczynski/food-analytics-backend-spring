package org.example.foodbudgetbackendspring.core.auth;

import org.example.foodbudgetbackendspring.core.auth.model.TokenType;
import org.example.foodbudgetbackendspring.core.auth.model.VerificationToken;
import org.example.foodbudgetbackendspring.core.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    void deleteByUserAndType(User user, TokenType type);

    Optional<VerificationToken> findByTokenAndUser_EmailAndType(String token, String email, TokenType type);
}
