package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class AuthorizeServiceImpl extends ServiceImpl<AccountMapper, Account> implements AuthorizeService {

    @Value("${spring.mail.username}")
    private String from;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private MailSender mailSender;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    /**
     * 获取验证码
     * @param email
     * @return
     */
    @Override
    public ResponseResult sendValidateEmail(String email,String sessionId) {
        String key="email:"+sessionId+":"+email;
        //判断是否小于60秒
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            Long expire = Optional.ofNullable(stringRedisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) return ResponseResult.errorResult(400,"请60秒后重试");

        }
        //生成验证码
        Random random = new Random();
        int code = random.nextInt(89999)+100000;
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(email);
        mailMessage.setSubject("欢迎加入Youthopia大家庭");
        mailMessage.setText("验证码(有效期3分钟):"+code);
        try {
            mailSender.send(mailMessage);
            stringRedisTemplate.opsForValue().set(key,String.valueOf(code),3,TimeUnit.MINUTES);
            return ResponseResult.okResult();
        } catch (MailException e){
            e.printStackTrace();
            return ResponseResult.errorResult(500,"发送失败，请联系管理员");

        }
    }
}
