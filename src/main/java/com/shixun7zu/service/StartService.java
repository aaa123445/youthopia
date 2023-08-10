package com.shixun7zu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun7zu.entity.Start;
import com.shixun7zu.entity.res.ResponseResult;


/**
 * (Start)表服务接口
 *
 * @author Jc
 * @since 2023-08-08 09:15:14
 */
public interface StartService extends IService<Start> {

    void delStart(String username,Integer id);

    ResponseResult<?> getStartList();

}

