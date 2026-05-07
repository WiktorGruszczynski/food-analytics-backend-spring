package org.example.foodbudgetbackendspring.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.foodbudgetbackendspring.user.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expires;

    public VerificationToken(String token, TokenType type, User user, int expiryInMinutes) {
        this.token = token;
        this.type = type;
        this.user = user;
        this.expires = LocalDateTime.now().plusMinutes(expiryInMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expires);
    }
}
