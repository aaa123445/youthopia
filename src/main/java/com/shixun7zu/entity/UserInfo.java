package com.shixun7zu.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (UserInfo)表实体类
 *
 * @author Jc
 * @since 2023-07-27 17:17:55
 */
@Data
@TableName("user_info")
public class UserInfo {
    @TableId
    private Integer id;
    @TableField("account_id")
    private Integer accountId;
    private String username;
    //昵称
    private String nickname;
    //头像
    private String avatar;
    //简介
    private String synopsis;
    //生日
    private Date birthday;
    //粉丝数
    @TableField("fans_count")
    private Integer fansCount;
    //关注数
    @TableField("concern_count")
    private Integer concernCount;
    //注册时间
    @TableField("create_time")
    private Date createTime;
    //地址
    private String address;

}

