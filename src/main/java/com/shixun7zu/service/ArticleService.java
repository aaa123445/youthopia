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

    ResponseResult<?> getArticleList(Integer num,Integer size,String type);

    ResponseResult<?> addArticle(Article article);
}

