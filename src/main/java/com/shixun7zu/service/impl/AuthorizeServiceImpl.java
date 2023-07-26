package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.enums.AppHttpCodeEnum;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.service.AuthorizeService;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    /**
     * 注册（携带验证码）
     * @param username
     * @param email
     * @param password
     * @param code
     * @return
     */
    @Override
    public ResponseResult addAccount(String username, String email, String password, String code,String sessionId) {
        String key="email:"+sessionId+":"+email;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            String s = stringRedisTemplate.opsForValue().get(key);
            if (s == null) return ResponseResult.errorResult(400,"验证码失效");
            if (s.equals(code)) {
                password = new BCryptPasswordEncoder().encode(password);
                Account account = new Account(username,email,password);
                if (save(account)) return ResponseResult.okResult(200,"注册成功");
            }else return ResponseResult.errorResult(400,"验证码错误");
        }
        return ResponseResult.errorResult(505,"内部错误，请联系管理员");
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
        //判断是否已注册
        if (getOne(new QueryWrapper<Account>().eq("email", email)) != null)
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
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
            log.info("邮件发送成功");
            stringRedisTemplate.opsForValue().set(key,String.valueOf(code),3,TimeUnit.MINUTES);
            return ResponseResult.okResult();
        } catch (MailException e){
            log.info(e.toString());
            return ResponseResult.errorResult(500,"发送失败，请联系管理员");

        }
    }
}
