package com.shixun7zu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shixun7zu.entity.Start;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * (UserStrat)表数据库访问层
 *
 * @author Jc
 * @since 2023-08-08 09:15:14
 */
public interface StartMapper extends BaseMapper<Start> {

    @Select("""
            select * from user_start where username = #{username}
            and article_id = #{id}
            and del = 1
            """)
    Start getStart(String username,Integer id);

    @Update("""
            UPDATE user_start SET del=0 WHERE username = #{username}
            AND article_id = #{id}
            """)
    void disDelStart(String username,Integer id);
}

