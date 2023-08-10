package com.shixun7zu.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shixun7zu.entity.Account;
import com.shixun7zu.mapper.AccountMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtils {



    public static String getUsername(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

}
