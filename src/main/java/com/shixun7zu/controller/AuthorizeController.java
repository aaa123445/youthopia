package com.shixun7zu.controller;

import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_REGEX = "^[A-Za-z0-9-._]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{4,16}$";

    @Resource
    private AuthorizeService authorizeService;

    @PostMapping("/valid-email")
    public ResponseResult validateEmail(@Pattern (regexp = EMAIL_REGEX)
                                            @RequestParam("email") String email,
                                        HttpSession session){
        return authorizeService.sendValidateEmail(email,session.getId());
    }

    @PostMapping("/register")
    public ResponseResult addAccount(@Pattern(regexp = USERNAME_REGEX)
                                         @Length(min = 2,max = 8)
                                         @RequestParam("username") String username,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @Length(min = 6,max = 6)
                                     @RequestParam("code") String code,
                                     HttpSession session){
        return authorizeService.addAccount(username,email,password,code,session.getId());
    }
}
