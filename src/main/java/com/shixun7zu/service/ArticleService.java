package com.shixun7zu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.tool.ResponseResult;

/**
 * (user_article)表服务接口
 *
 * @author Jc
 * @since 2023-07-27 09:16:14
 */
public interface ArticleService extends IService<Article> {

    ResponseResult<?> getArticleList(Integer num,Integer size,Integer status);
    ResponseResult<?> getArticleListAfterLogin(Integer num, Integer size, Integer status);

    ResponseResult<?> addArticle(Article article);

    ResponseResult<?> delArticleById(Integer id);

    ResponseResult<?> getArticleByOwn(Integer num,Integer size);

    ResponseResult<?> addStart(Integer id);
    ResponseResult<?> delStart(Integer id);
}

