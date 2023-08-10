package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.entity.vo.UserInfoVo;
import com.shixun7zu.enums.AppHttpCodeEnum;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.mapper.ArticleMapper;
import com.shixun7zu.service.UserService;
import com.shixun7zu.util.BeanCopyUtils;
import com.shixun7zu.util.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<AccountMapper, Account> implements UserService {
    @Resource
    private AccountMapper accountMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult<?> getInfo() {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUsername, SecurityUtils.getUsername());
        Account account = accountMapper.selectOne(queryWrapper);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(account, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult<?> getAvatar(String text) {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUsername, text)
                .or()
                .eq(Account::getEmail, text);
        Account account = getOne(queryWrapper);
        if (account == null) return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_UNEXCITED);
        return ResponseResult.okResult(account.getAvatar());

    }

    @Override
    public ResponseResult<?> updateInfo(UserInfoVo userInfoVo) {
        LambdaUpdateWrapper<Account> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Account::getUsername, SecurityUtils.getUsername())
                .set(Account::getNickname, userInfoVo.getNickname())
                .set(Account::getAvatar, userInfoVo.getAvatar())
                .set(Account::getSynopsis, userInfoVo.getSynopsis())
                .set(Account::getGender, userInfoVo.getGender())
                .set(Account::getBirthday, userInfoVo.getBirthday())
                .set(Account::getAddress, userInfoVo.getAddress());
        LambdaUpdateWrapper<Article> articleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        articleLambdaUpdateWrapper.set(Article::getAvatar, userInfoVo.getAvatar())
                .set(Article::getNickname, userInfoVo.getNickname())
                .inSql(Article::getAccountId,
                        "select id from user_account where username=" +
                                "'" + SecurityUtils.getUsername() + "'");
        if (update(updateWrapper)) {
            articleMapper.update(null, articleLambdaUpdateWrapper);
            return ResponseResult.okResult();
        } else return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public Integer getUserId() {
        Account account = getOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getUsername, SecurityUtils.getUsername()));
        return account.getId();
    }
}
