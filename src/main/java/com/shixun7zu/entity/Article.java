package com.shixun7zu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * (UserArticle)表实体类
 *
 * @author Jc
 * @since 2023-07-27 17:07:41
 */
@Data
@TableName("user_article")
public class Article {
    @TableId
    private Integer id;
    //创建人
    @TableField("account_id")
    private Integer accountId;
    private String avatar;
    private String nickname;
    //创建时间
    @TableField("create_time")
    private Date createTime;
    //文章详细
    @TableField("article_detailed")
    private String articleDetailed;
    //文章图片
    @TableField("article_images")
    private String articleImages;
    //点赞数
    private Integer start;
    //评论数
    @TableField("comments_count")
    private Integer commentsCount;
    //转发数
    @TableField("forwards_count")
    private Integer forwardsCount;
    //文章类别:默认是原创
    @TableField("article_type")
    private String articleType;
    //0:草稿；1:正式文章
    private Integer status;

    private Integer del;

}

