package com.shixun7zu.entity;

import java.util.Date;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户关注关系表(UserFollower)表实体类
 *
 * @author Jc
 * @since 2023-08-10 10:11:56
 */

@Data
@TableName("user_follower")
public class Follower {
    @TableId
    private Integer id;
    //被关注者id
    @TableField("be_account_id")
    private Integer beAccountId;
    @TableField("account_id")
    private Integer accountId;
    //最后变更时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
    //关注关系存续状态，0-存在关注关系，1-取消关注
    private Integer del;
}

