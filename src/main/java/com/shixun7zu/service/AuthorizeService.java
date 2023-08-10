package com.shixun7zu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.res.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends IService<Account>, UserDetailsService {
    ResponseResult<?> addAccount(String username,
                              String email,
                              String password,
                              String code,
                              String sessionId);

    ResponseResult<?> sendValidateEmail(String email, String sessionId);

    ResponseResult<?> findPassword(String email,String password, String code ,String sessionId);
}
