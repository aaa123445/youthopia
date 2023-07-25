package com.shixun7zu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.tool.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account> , UserDetailsService {
    ResponseResult addAccount(Account account);
}
