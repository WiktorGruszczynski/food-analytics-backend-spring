package org.example.foodbudgetbackendspring.auth;

import org.example.foodbudgetbackendspring.auth.model.TokenType;
import org.example.foodbudgetbackendspring.auth.model.VerificationToken;
import org.example.foodbudgetbackendspring.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, UUID> {
    void deleteByUser(User user);

    Optional<VerificationToken> findByTokenAndUser_EmailAndType(String token, String email, TokenType type);
}
