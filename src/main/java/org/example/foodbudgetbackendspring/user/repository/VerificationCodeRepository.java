package org.example.foodbudgetbackendspring.user.repository;

import org.example.foodbudgetbackendspring.user.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByCodeAndUserId(String code, Long userId);
}
