package com.shixun7zu.controller;

import com.shixun7zu.entity.Follower;
import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.service.FollowerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follower")
public class FollowerController {
    @Resource
    private FollowerService followerService;

    @PostMapping("/add")
    @ResponseBody
    public ResponseResult<?> addFollower(@RequestBody Follower follower){
        return followerService.addFollower(follower);
    }
    @GetMapping("/get-fans")
    @ResponseBody
    public ResponseResult<?> getFans(Integer pageNum,Integer pageSize){
        return followerService.getFans(pageNum,pageSize);
    }
}
