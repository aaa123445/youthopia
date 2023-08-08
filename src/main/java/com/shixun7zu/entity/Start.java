package com.shixun7zu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * (UserStrat)表实体类
 *
 * @author Jc
 * @since 2023-08-08 09:15:14
 */
@Data
@TableName("user_start")
public class Start {
    
    private Integer id;
    
    private String username;

    @TableField("article_id")
    private Integer articleId;
    private Integer del;
    public Start(){}

    public Start(String username,Integer articleId){
        this.username = username;
        this.articleId = articleId;
    }
}

