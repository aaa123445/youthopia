package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Start;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.entity.vo.StartListVo;
import com.shixun7zu.mapper.StartMapper;
import com.shixun7zu.service.StartService;
import com.shixun7zu.uilit.BeanCopyUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StartServiceImpl extends ServiceImpl<StartMapper, Start> implements StartService {

    @Resource
    private StartMapper startMapper;
    @Override
    public void delStart(String username,Integer id) {
        LambdaUpdateWrapper<Start> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Start::getArticleId,id)
                .eq(Start::getUsername,username);
        remove(updateWrapper);
    }

    @Override
    public ResponseResult<?> getStartList() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Start> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Start::getUsername,user.getUsername())
                .select(Start::getArticleId);
        List<Start> starts = startMapper.selectList(queryWrapper);
        List<Integer> res = new ArrayList<>();
        starts.forEach(start -> res.add(start.getArticleId()));
        return ResponseResult.okResult(res);
    }



    //    /**
//     * 将返回结果优化为只有id
//     */
//    private List<Integer> copyId(List<Start> list){
//        List<Integer> res = new ArrayList<>();
//        list.forEach(start -> res.add(start.getArticleId()));
//        return res;
//    }
}
