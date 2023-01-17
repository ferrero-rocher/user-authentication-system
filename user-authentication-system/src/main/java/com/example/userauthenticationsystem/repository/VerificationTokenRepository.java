package com.example.userauthenticationsystem.repository;

import com.example.userauthenticationsystem.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity,Integer> {
    VerificationTokenEntity findByToken(String token);

    VerificationTokenEntity findByUserId(int userId);
}
