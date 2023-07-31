package com.shixun7zu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("user_account")
@Data
public class Account {
    @TableId
    private Integer id;
    private String email;
    private String username;
    private String password;
    private Integer del;
    @TableField("create_time")
    private Date creatTime;

    public Account(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
