package com.shixun7zu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.Follower;
import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.entity.vo.FansVo;
import com.shixun7zu.entity.vo.PageVo;
import com.shixun7zu.mapper.FollowerMapper;
import com.shixun7zu.service.FollowerService;
import com.shixun7zu.service.UserService;
import com.shixun7zu.util.BeanCopyUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户关注关系表(UserFollower)表服务实现类
 *
 * @author Jc
 * @since 2023-08-10 10:11:56
 */
@Service
public class FollowerServiceImpl extends ServiceImpl<FollowerMapper, Follower> implements FollowerService {

    @Resource
    private UserService userService;
    @Override
    public ResponseResult<?> addFollower(Follower follower) {
        follower.setUpdateTime(new Date());
        follower.setAccountId(userService.getUserId());
        save(follower);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getFans(Integer pageNum, Integer pageSize) {
        Integer userId = userService.getUserId();
        LambdaQueryWrapper<Follower> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follower::getBeAccountId,userId)
                .orderByDesc(Follower::getUpdateTime);
        Page<Follower> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        PageVo res = new PageVo(toFansVoList(page.getRecords()),page.getTotal());
        return ResponseResult.okResult(res);
    }
    //获取昵称和头像
    private List<FansVo> toFansVoList(List<Follower> followerList){
        List<FansVo> res = new ArrayList<>();
        followerList.forEach(follower->{
            Account byId = userService.getById(follower.getAccountId());
            res.add(new FansVo(
                    follower.getId(),
                    follower.getAccountId(),
                    byId.getAvatar(),
                    byId.getNickname(),
                    follower.getUpdateTime()));
        });

        return res;
    }
}

