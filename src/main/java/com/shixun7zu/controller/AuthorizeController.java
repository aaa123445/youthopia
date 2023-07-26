package com.shixun7zu.controller;

import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final static String EMAIL_REGEX = "^[A-Za-z0-9-._]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$";

    @Resource
    private AuthorizeService authorizeService;

    @PostMapping("/valid-email")
    public ResponseResult validateEmail(@Pattern (regexp = EMAIL_REGEX)
                                            @RequestParam("email") String email,
                                        HttpSession session){
        return authorizeService.sendValidateEmail(email,session.getId());
    }

    @PostMapping("/register")
    public ResponseResult addAccount(@RequestBody Account account){
        return authorizeService.addAccount(account);
    }
}
