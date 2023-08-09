package com.shixun7zu.controller;

import com.shixun7zu.entity.Comment;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;
    @GetMapping("/get-list")
    @ResponseBody
    public ResponseResult<?> getComment(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(articleId,pageNum,pageSize);
    }
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult<?> addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}
