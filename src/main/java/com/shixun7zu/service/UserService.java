package com.shixun7zu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.entity.vo.UserInfoVo;

public interface UserService extends IService<Account> {
    ResponseResult<?> getInfo();

    ResponseResult<?> getAvatar(String text);

    ResponseResult<?> updateInfo(UserInfoVo userInfoVo);

    Integer getUserId();
}
