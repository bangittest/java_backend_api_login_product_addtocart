package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.ResponseDto;
import com.ecommerce.demo.dto.user.SignInDto;
import com.ecommerce.demo.dto.user.SignupDto;
import com.ecommerce.demo.dto.user.SingInReponseDto;
import com.ecommerce.demo.exceptions.AuthenticationFailException;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.reponsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {
        // Check if the user is already present
        User existingUser = userRepository.findByEmail(signupDto.getEmail());
        if (existingUser != null) {
            return new ResponseDto("error", "User already exists");
        }

        // Hash the password
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new ResponseDto("error", "Password hashing failed");
        }

        User user = new User(signupDto.getFirstName(),
                signupDto.getLastName(),
                signupDto.getEmail(),
                encryptedPassword);

        userRepository.save(user);

        //save the user
        //create the token
        final AuthenticationToken authenticationToken=new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        return new ResponseDto("success", "User registered successfully");
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SingInReponseDto signIn(SignInDto signInDto) {
    //find user by email

        User user=userRepository.findByEmail(signInDto.getEmail());
        if (Objects.isNull(user)){
            throw new AuthenticationFailException("user is not valid");
        }

        //hash the password
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
                throw new AuthenticationFailException("wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //compare the password in Db

        //if password match
        AuthenticationToken token=authenticationService.getToken(user);
        //retrive the token

        if (Objects.isNull(token)){
            throw new CustomException("tokrn is not present");
        }
        return new SingInReponseDto("succes",token.getToken());
        //return response

    }
}

