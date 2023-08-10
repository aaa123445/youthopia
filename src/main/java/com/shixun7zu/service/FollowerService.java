package com.shixun7zu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun7zu.entity.Follower;
import com.shixun7zu.entity.res.ResponseResult;


/**
 * 用户关注关系表(UserFollower)表服务接口
 *
 * @author Jc
 * @since 2023-08-10 10:11:56
 */
public interface FollowerService extends IService<Follower> {

    ResponseResult<?> addFollower(Follower follower);

    ResponseResult<?> getFans(Integer pageNum, Integer pageSize);
}

