package com.shixun7zu.controller;

import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.ArticleService;
import com.shixun7zu.uilit.TencentCOSUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文章Controller
 */
@RestController
@RequestMapping("/api/article")
@Slf4j
public class ArticleController {
    @Resource
    private ArticleService articleService;


    @GetMapping("/article-list")
    @ResponseBody
    public ResponseResult<?> getArticleList(Integer num,Integer size,Integer status){
        return articleService.getArticleList(num,size,status);
    }

    @PostMapping("/article-images")
    @ResponseBody
    public ResponseResult<?> uploadImages(MultipartFile file){
        return ResponseResult.okResult(TencentCOSUtil.upLoadImages(file));
    }

    @PostMapping("/add-article")
    @ResponseBody
    public ResponseResult<?> addArticle(@RequestBody Article article){
        return articleService.addArticle(article);
    }


}
