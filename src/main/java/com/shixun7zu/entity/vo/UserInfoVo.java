package com.shixun7zu.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserInfoVo {
    //昵称
    private String nickname;
    //头像
    private String avatar;
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
    //注册时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("create_time")
    private Date createTime;
    //地址
    private String address;
}
