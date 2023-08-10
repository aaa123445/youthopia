package com.shixun7zu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 评论表(UserComment)表实体类
 *
 * @author Jc
 * @since 2023-08-09 19:06:09
 */

@Data
@TableName("user_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;

    //文章id
    @TableField("article_id")
    private Integer articleId;
    //根评论id
    private Integer rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    @TableField("to_comment_user_id")
    private Integer toCommentUserId;
    //回复目标评论id
    @TableField("to_comment_id")
    private Integer toCommentId;

    private Integer createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer del;


}

