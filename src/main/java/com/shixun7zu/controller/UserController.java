package com.shixun7zu.controller;

import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.uilit.TencentCOSUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @CrossOrigin
    @PostMapping("/upload-avatar")
    @ResponseBody
    public ResponseResult<?> uploadAvatar(MultipartFile file){
        return ResponseResult.okResult(TencentCOSUtil.upLoadAvatar(file));
    }
}
