package com.shixun7zu.controller;

import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.ArticleService;
import com.shixun7zu.uilit.TencentCOSUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Article", description = "文章相关")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @Operation(summary = "获取文章列表")
    @GetMapping("/article-list")
    @ResponseBody
    public ResponseResult<?> getArticleList(Integer num,Integer size,String type){
        return articleService.getArticleList(num,size,type);
    }

    @PostMapping("/article-images")
    @ResponseBody
    public ResponseResult<?> uploadImages(MultipartFile file){
        return ResponseResult.okResult(TencentCOSUtil.upLoadImages(file));
    }

    @PostMapping("add-article")
    @ResponseBody
    public ResponseResult<?> addArticle(@RequestBody Article article){
        return articleService.addArticle(article);
    }

}
