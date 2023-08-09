package com.shixun7zu.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (UserComment)表实体类
 *
 * @author Jc
 * @since 2023-08-09 15:23:01
 */
@Data
@TableName("user_comment")
public class Comment {
    
    private Integer id;
    //文章id
    private Integer articleId;
    //根评论id
    private Integer rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Integer toCommentUserId;
    //回复目标评论id
    private Integer toCommentId;
    
    private Integer createBy;
    
    private Date createTime;
    
    private Integer del;

}

