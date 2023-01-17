package com.example.userauthenticationsystem.controller;

import com.example.userauthenticationsystem.config.MQConfig;
import com.example.userauthenticationsystem.entity.ActivityEntity;
import com.example.userauthenticationsystem.entity.PasswordResetTokenEntity;
import com.example.userauthenticationsystem.entity.UserEntity;
import com.example.userauthenticationsystem.entity.VerificationTokenEntity;
import com.example.userauthenticationsystem.errorhandler.NoRecordFoundException;
import com.example.userauthenticationsystem.errorhandler.RecordAlreadyExistsException;
import com.example.userauthenticationsystem.model.PasswordModel;
import com.example.userauthenticationsystem.model.UserModel;
import com.example.userauthenticationsystem.service.UserServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("api/v1")
public class RegistrationController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RabbitTemplate template;

    @PostMapping("/register")
    public ResponseEntity <String> registerUser(@RequestBody @Valid UserModel userModel, final HttpServletRequest request)
    {   //check if user with that email exists
        //1.create entry in user table
        //2.create entry in verification url entity
        //3.send url to user
        boolean isEmailDuplicate= this.userService.checkIfEmailExists(userModel.getEmail());
        // call activity
        postToMQ("REGISTER", userModel.getEmail()+" tried to register");


        if(isEmailDuplicate)
        {
            throw new RecordAlreadyExistsException("Duplicate Email ID found: "+ userModel.getEmail());
        }
        UserEntity userEntity = this.userService.registerUser(userModel);
        String result = this.userService.saveVerificationToken(userEntity);
        String verificationUrl = this.userService.sendEmail(result,request,"validateverification");
       // System.out.println(result);
        return new ResponseEntity<>(verificationUrl, HttpStatus.OK);
    }

    @GetMapping ("verifyRegistration")
    public ResponseEntity <String> verifyToken(@RequestParam("token") String token)
    {
        String result = this.userService.verifyTokenValidity(token);

        if(result.equalsIgnoreCase("valid"))
        {
            postToMQ("VERIFICATION SUCCESS",token + " is verified successfully"  );
            return new ResponseEntity<>("Congratulations..!! you are now verified succcessfuly", HttpStatus.OK);
        }
        postToMQ("VERIFICATION FAILED","Verification failed for token ID: "+token   );
         return new ResponseEntity<>("Verification failed", HttpStatus.NOT_FOUND);

    }
    @GetMapping("/resendVerificationURL")
    public ResponseEntity <?> resendVerificationURL(@RequestParam("userid") String userid, final HttpServletRequest request)
    {
        VerificationTokenEntity result = userService.findUser(Integer.parseInt(userid));
        postToMQ("RESEND VERIFICATION",userid + " made another request to resend verification token");
        if(result==null)
        {
            throw new NoRecordFoundException("User with given user id not found: " + userid);
        }
        String verificationUrl = this.userService.sendEmail(result.getToken(),request,"validateverification");
        return new ResponseEntity<>(verificationUrl, HttpStatus.OK);

    }

    @GetMapping("/forgotPassword")
    public ResponseEntity <?> forgotPassword(@RequestParam("email") String email, final HttpServletRequest request)
    {
        UserEntity userEntity = userService.findUserByEmail(email);
        postToMQ("FORGOT PASSWORD",email + " made request to reset his password");

        if (userEntity == null || userEntity.isEnabled()==false)
        {
            //return new ResponseEntity<>(new NoSuchFieldException().toString(), HttpStatus.NOT_FOUND);
                postToMQ("FORGOT PASSWORD",email + " does not exist or not verified");
                throw new NoRecordFoundException("User with given email id not found or not verified: " + email);

        }
        PasswordResetTokenEntity passwordResetTokenEntity =this.userService.savePasswordResetToken(userEntity);
        String passwordChangeUrl=this.userService.sendEmail(passwordResetTokenEntity.getToken(),request,"changepassword");
        postToMQ("PASSWORD CHANGED SUCESSFULLY",email + "'s paswword reseted");
        return new  ResponseEntity<>(passwordChangeUrl, HttpStatus.OK);

    }

    @PostMapping("/savePassword")
    public ResponseEntity <String> savePassword(@RequestParam("token") String token , @RequestBody PasswordModel passwordModel)
    {
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid"))
        {
            return new ResponseEntity<>("Invalid Token", HttpStatus.NOT_FOUND);

        }

        Optional<UserEntity> user = userService.getUserByPasswordResetToken(token);
        if(!user.isPresent())
        {

            postToMQ("FORGOT PASSWORD FAILED", user.get().getEmail() +" does not exist");
            throw new NoRecordFoundException("User not found");


        }
        userService.changePassword(user.get(),passwordModel.getNewPassword());
        postToMQ("FORGOT PASSWORD SUCCESS", user.get().getEmail() +" changed his password succesfully");
        return new ResponseEntity<>("Password reseted sucesfully", HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/user/changePassword")
    public ResponseEntity <String>  changePassword(@RequestBody @Valid PasswordModel passwordModel)
    {
        UserEntity user = userService.findUserByEmail(passwordModel.getEmail());
        if (user == null)
        {
            postToMQ("CHANGE PASSWORD FAILED", user.getEmail() +" does not exist");
            throw new NoRecordFoundException("User with given email id not found: " + passwordModel.getEmail());
        }
        if(!userService.checkIfValidPassword(user,passwordModel.getOldPassword()))
        {
            postToMQ("CHANGE PASSWORD FAILED", user.getEmail() +" old and new password doesnt match");
            return new ResponseEntity<>("Invalid Old PassWord", HttpStatus.NOT_FOUND);
        }
        //Save new Password
        userService.changePassword(user,passwordModel.getNewPassword());
        postToMQ("CHANGE PASSWORD SUCCESS", user.getEmail() +" changed his password succesfully");
        return new ResponseEntity<>("Password changed succesfully", HttpStatus.OK);
    }

    public void postToMQ(String name , String shortDesc)
    {
        ActivityEntity activity = new ActivityEntity(name,shortDesc);
        template.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY,activity);
    }


}
