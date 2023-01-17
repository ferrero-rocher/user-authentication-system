package com.example.userauthenticationsystem.service;

import com.example.userauthenticationsystem.entity.PasswordResetTokenEntity;
import com.example.userauthenticationsystem.entity.UserEntity;
import com.example.userauthenticationsystem.entity.VerificationTokenEntity;
import com.example.userauthenticationsystem.model.UserModel;
import com.example.userauthenticationsystem.repository.PasswordResetTokenRepository;
import com.example.userauthenticationsystem.repository.UserRepository;
import com.example.userauthenticationsystem.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
   /* @Autowired
    private PasswordEncoder passwordEncoder;*/
    @Autowired
    UserRepository userRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserEntity registerUser(UserModel userModel) {
        UserEntity user = new UserEntity();
        user.setEmail(userModel.getEmail());
        user.setFirstname(userModel.getFirstname());
        user.setLastname(userModel.getLastname());
        user.setRole("ROLE_USER");
        user.setUsername(userModel.getUsername());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));//encrypting the password

        userRepository.save(user);
        return user;

    }

    @Override
    public String saveVerificationToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity(userEntity,token);
        verificationTokenRepository.save(verificationTokenEntity);
        return token;

    }

    @Override
    public String sendEmail(String token,final HttpServletRequest request,String type) {
        String url="";
        if(type.equalsIgnoreCase("validateverification"))
        {
            url = createVerificationURL(request) +"verifyRegistration?token=" +token;
        }
        else
        {
            url = createVerificationURL(request) +"savePassword?token=" +token;
        }

        log.info(url);
        //System.out.println(url);
        return url;

    }

    @Override
    public String verifyTokenValidity(String token) {
        VerificationTokenEntity verificationToken=verificationTokenRepository.findByToken(token);
        if(verificationToken == null)//no user found
        {
            return "invalid";
        }
        UserEntity user = verificationToken.getUser();//extract the userentity
        //logic to check if token is expired
        Calendar calendar = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <=0)
        {
            return "Token expiered";
        }
        user.setEnabled(true);//set the enabled field to true in the DB
        userRepository.save(user);//save the updated user
        return "valid";
    }

    @Override
    public VerificationTokenEntity findUser(int userId) {
        VerificationTokenEntity verificationToken=verificationTokenRepository.findByUserId(userId);
        return verificationToken;

    }

    @Override
    public UserEntity findUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);


        return user;
    }

    @Override
    public PasswordResetTokenEntity savePasswordResetToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity(userEntity,token);
        passwordResetTokenRepository.save(passwordResetTokenEntity );
        return passwordResetTokenEntity;
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetTokenEntity passwordResetToken=passwordResetTokenRepository.findByToken(token);//Get the Verification Table object based on the token
        if(passwordResetToken == null)//no user found
        {
            return "invalid";
        }
        UserEntity user = passwordResetToken.getUser();//extract the userentity
        Calendar calendar = Calendar.getInstance();
        if(passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <=0)
        {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "Token expired";
        }

        return "valid";

    }

    @Override
    public Optional<UserEntity> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(UserEntity userEntity, String newPassword) {
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    @Override
    public boolean checkIfValidPassword(UserEntity user, String oldPassword) {
        if(passwordEncoder.matches(oldPassword, user.getPassword()))
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        if(userRepository.findByEmail(email)!=null)
        {
            return true;
        }
        return false;
    }


    private String createVerificationURL(HttpServletRequest request) {

        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                "/" +
                request.getContextPath();
    }

}
