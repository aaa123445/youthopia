package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.entity.vo.ArticleListVo;
import com.shixun7zu.enums.AppHttpCodeEnum;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.mapper.ArticleMapper;
import com.shixun7zu.service.ArticleService;
import com.shixun7zu.uilit.BeanCopyUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * (user_article)表服务实现类
 *
 * @author Jc
 * @since 2023-07-27 09:16:14
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private AccountMapper accountMapper;

    /**
     * 获取文章列表
     *
     * @param num  页码
     * @param size 每页大小
     * @param status 文章权限
     * @return list
     */
    @Override
    public ResponseResult<?> getArticleList(Integer num, Integer size, Integer status) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, status)
                .orderByDesc(Article::getCreateTime);
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
        LambdaQueryWrapper<Account> accountLambdaQueryWrapper = new LambdaQueryWrapper<>();
        accountLambdaQueryWrapper.select(Account::getAvatar,
                        Account::getNickname,
                        Account::getId)
                .eq(Account::getUsername, user.getUsername());
        Account account = accountMapper.selectOne(accountLambdaQueryWrapper);
        article.setAccountId(account.getId());
        article.setAvatar(account.getAvatar());
        article.setNickname(account.getNickname());
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        article.setCreateTime(ft.format(new Date()));
        log.info(ft.format(new Date()));
        if (save(article)) return ResponseResult.okResult();
        else return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult<?> delArticleById(Integer id) {
        if (removeById(id)) return ResponseResult.okResult();
        else return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult<?> getArticleByOwn(Integer num,Integer size) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Article::getAccountId,
                "select id from user_account where username=" +
                        "'" + user.getUsername() + "'");
        IPage<Article> page = articleMapper.selectPage(new Page<>(num, size), queryWrapper);
        return ResponseResult.okResult(BeanCopyUtils
                .copyBeanList(page.getRecords(), ArticleListVo.class));
    }
}

