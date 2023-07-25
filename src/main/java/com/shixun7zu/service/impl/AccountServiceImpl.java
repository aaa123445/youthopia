package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService{

    @Resource
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",username).or().eq("username",username);
        Account account = getOne(queryWrapper);
        if (account == null) throw new UsernameNotFoundException("用户名密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public ResponseResult addAccount(Account account) {
        if (accountMapper.selectList(new QueryWrapper<Account>().eq("username",account.getUsername())
                .or()
                .eq("email",account.getEmail())).size()>0) return ResponseResult.errorResult(500,"用户名已存在");
        if ((account.getUsername() == null || account.getEmail() == null) &&
        account.getPassword() == null) return ResponseResult.errorResult(500,"用户名密码不能为空");
        account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
        if (save(account)) return ResponseResult.okResult(200,"注册成功");
        else return ResponseResult.errorResult(504,"未知错误请联系管理员");
    }
}
