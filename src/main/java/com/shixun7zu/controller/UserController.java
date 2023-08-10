package com.shixun7zu.controller;

import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.entity.vo.UserInfoVo;
import com.shixun7zu.service.ArticleService;
import com.shixun7zu.service.UserService;
import com.shixun7zu.util.TencentCOSUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;

    @PostMapping("/upload-avatar")
    @ResponseBody
    public ResponseResult<?> uploadAvatar(MultipartFile file){
        return ResponseResult.okResult(TencentCOSUtil.upLoadAvatar(file));
    }

    @GetMapping("/user-info")
    @ResponseBody
    public ResponseResult<?> userInfo(){
        return userService.getInfo();
    }
    @PostMapping("/update-info")
    @ResponseBody
    public ResponseResult<?> updateInfo(@RequestBody UserInfoVo userInfoVo){
        return userService.updateInfo(userInfoVo);
    }
    @GetMapping("/get-article-own")
    @ResponseBody
    public ResponseResult<?> getArticleByOwn(Integer num,Integer size){
        return articleService.getArticleByOwn(num,size);
    }
    @GetMapping("/del-article")
    @ResponseBody
    public ResponseResult<?> delArticleById(Integer id){
        return articleService.delArticleById(id);
    }
}
