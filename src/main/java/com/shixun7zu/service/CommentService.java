package com.shixun7zu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun7zu.entity.Comment;
import com.shixun7zu.entity.tool.ResponseResult;


/**
 * (Comment)表服务接口
 *
 * @author Jc
 * @since 2023-08-09 15:23:02
 */
public interface CommentService extends IService<Comment> {

    ResponseResult<?> commentList(Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult<?> addComment(Comment comment);
}

