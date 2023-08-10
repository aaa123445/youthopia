package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.enums.AppHttpCodeEnum;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.service.AuthorizeService;
import jakarta.annotation.Resource;
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

import java.util.Date;
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
        queryWrapper.eq("email", username).or().eq("username", username);
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
     *
     * @param username 用户名
     * @param email    邮箱
     * @param password 密码
     * @param code     验证码
     * @return ResponseResult
     */
    @Override
    public ResponseResult<?> addAccount(String username, String email, String password, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email;
        //验证用户是否存在
        if (accountMapper.selectOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getUsername, username)
                .or()
                .eq(Account::getEmail, email)) != null)
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        //是否获取过验证码
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            String s = stringRedisTemplate.opsForValue().get(key);
            //验证码是否过期
            if (s == null) return ResponseResult.errorResult(400, "验证码失效");
            //验证码是否正确
            if (s.equals(code)) {
                password = new BCryptPasswordEncoder().encode(password);
                Account account = new Account(email, username, password);
                account.setCreateTime(new Date());
                if (save(account)) return ResponseResult.okResult(200, "注册成功");
            } else return ResponseResult.errorResult(400, "验证码错误");
        }
        return ResponseResult.errorResult(500, "未获取验证码");
    }

    /**
     * 获取验证码
     *
     * @param email 邮箱
     * @return ResponseResult
     */
    @Override
    public ResponseResult<?> sendValidateEmail(String email, String sessionId) {
        String key = "email:" + sessionId + ":" + email;
        //判断是否小于60秒
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            Long expire = Optional.ofNullable(stringRedisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) return ResponseResult.errorResult(400, "请60秒后重试");
        }
        //生成验证码
        Random random = new Random();
        int code = random.nextInt(89999) + 100000;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(email);
        mailMessage.setSubject("Youthopia官方");
        mailMessage.setText("这是您的验证码(有效期3分钟):" + code);
        try {
            mailSender.send(mailMessage);
            log.info("邮件发送成功");
            stringRedisTemplate.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);
            return ResponseResult.okResult();
        } catch (MailException e) {
            log.info(e.toString());
            return ResponseResult.errorResult(500, "发送失败，请联系管理员");
        }
    }

    @Override
    public ResponseResult<?> findPassword(String email, String password, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email;
        if (accountMapper.selectOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getEmail, email)) == null) return ResponseResult.errorResult(500, "邮箱不存在");
        //是否获取过验证码
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            String s = stringRedisTemplate.opsForValue().get(key);
            //验证码是否过期
            if (s == null) return ResponseResult.errorResult(400, "验证码失效");
            //验证码是否正确
            if (s.equals(code)) {
                password = new BCryptPasswordEncoder().encode(password);
                LambdaUpdateWrapper<Account> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Account::getEmail, email)
                        .set(Account::getPassword, password);
                if (update(updateWrapper)) return ResponseResult.okResult(200, "修改成功");
            } else return ResponseResult.errorResult(400, "验证码错误");
        }
        return ResponseResult.errorResult(500, "错误");
    }
}
