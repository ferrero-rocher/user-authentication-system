package com.example.userauthenticationsystem.repository;

import com.example.userauthenticationsystem.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity,Integer> {
    PasswordResetTokenEntity findByToken(String token);
}
