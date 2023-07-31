package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.UserInfo;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.entity.vo.ArticleListVo;
import com.shixun7zu.enums.AppHttpCodeEnum;
import com.shixun7zu.mapper.ArticleMapper;
import com.shixun7zu.mapper.UserInfoMapper;
import com.shixun7zu.service.ArticleService;
import com.shixun7zu.uilit.BeanCopyUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (user_article)表服务实现类
 *
 * @author Jc
 * @since 2023-07-27 09:16:14
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 获取文章列表
     *
     * @param num  页码
     * @param size 每页大小
     * @param type 文章类型
     * @return list
     */
    @Override
    public ResponseResult<?> getArticleList(Integer num, Integer size, String type) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Article::getStart, 500)
                .eq(Article::getStatus, 1)
                .eq(Article::getArticleType, type);
//        List<Article> articleList = list(queryWrapper);
//        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);
        IPage<Article> page = articleMapper.selectPage(new Page<>(num, size), queryWrapper);
        return ResponseResult.okResult(BeanCopyUtils
                .copyBeanList(page.getRecords(), ArticleListVo.class));
    }

    /**
     * 发文章
     *
     * @param article {
     *                "articleDetailed":"test",
     *                "articleImages":"test",
     *                "articleType":"test",
     *                "status":1
     *                }
     * @return ResponseResult
     */
    @Override
    public ResponseResult<?> addArticle(Article article) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.select(UserInfo::getAvatar,
                        UserInfo::getNickname,
                        UserInfo::getAccountId)
                .eq(UserInfo::getUsername, user.getUsername());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        article.setAccountId(userInfo.getAccountId());
        article.setAvatar(userInfo.getAvatar());
        article.setNickname(userInfo.getNickname());
        article.setCreateTime(new Date());
        if (save(article)) return ResponseResult.okResult(article);
        else return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}

