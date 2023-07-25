package com.shixun7zu.controller;

import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseResult addAccount(@RequestBody Account account){
        return accountService.addAccount(account);
    }
}
