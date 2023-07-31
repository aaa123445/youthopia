package com.shixun7zu.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 文章列表VO
 */
@Data
public class ArticleListVo {
    //头像
    private String avatar;
    //昵称
    private String nickname;
    //创建时间
    private Date createTime;
    //文章详细
    private String articleDetailed;
    //转发数
    private Integer forwardsCount;
    //评论数
    private Integer commentsCount;
    //点赞数
    private Integer start;

}
