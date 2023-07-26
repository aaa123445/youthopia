package com.shixun7zu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user_account")
@Data
public class Account {
    @TableId
    private Integer id;
    private String email;
    private String username;
    private String password;
    private Integer del;
}
