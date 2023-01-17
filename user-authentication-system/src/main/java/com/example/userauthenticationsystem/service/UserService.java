package com.example.userauthenticationsystem.service;

import com.example.userauthenticationsystem.entity.PasswordResetTokenEntity;
import com.example.userauthenticationsystem.entity.UserEntity;
import com.example.userauthenticationsystem.entity.VerificationTokenEntity;
import com.example.userauthenticationsystem.model.UserModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserService {
    UserEntity registerUser(UserModel userModel);

    String saveVerificationToken(UserEntity userEntity);

    String sendEmail(String res, HttpServletRequest request, String type);

    String verifyTokenValidity(String token);

    VerificationTokenEntity findUser(int userId);

    UserEntity findUserByEmail(String email);

    PasswordResetTokenEntity savePasswordResetToken(UserEntity userEntity);

    String validatePasswordResetToken(String token);

    Optional<UserEntity> getUserByPasswordResetToken(String token);

    void changePassword(UserEntity userEntity, String newPassword);

    boolean checkIfValidPassword(UserEntity user, String oldPassword);

    boolean checkIfEmailExists(String email);
}
