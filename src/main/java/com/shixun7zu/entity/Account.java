package com.shixun7zu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("user_account")
@Data
public class Account {
    @TableId
    private Integer id;
    private String email;
    private String username;
    private String password;
    //注册时间
    @TableField("create_time")
    private Date createTime;
    //头像
    private String avatar;
    //昵称
    private String nickname;
    //简介
    private String synopsis;
    //性别
    private String gender;
    //生日
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    //粉丝数
    @TableField("fans_count")
    private Integer fansCount;
    //关注数
    @TableField("concern_count")
    private Integer concernCount;
    private String address;
    private Integer del;


    public Account(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
