package com.ecommerce.demo.controller;

import com.ecommerce.demo.dto.ResponseDto;
import com.ecommerce.demo.dto.user.SignInDto;
import com.ecommerce.demo.dto.user.SignupDto;
import com.ecommerce.demo.dto.user.SingInReponseDto;
import com.ecommerce.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    //two apis

    //signup

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupDto signupDto){
        return userService.signUp(signupDto);
    }

    //signin
    @PostMapping("/signin")
    public SingInReponseDto signIn(@RequestBody SignInDto signInDto){
        return userService.signIn(signInDto);
    }

}
