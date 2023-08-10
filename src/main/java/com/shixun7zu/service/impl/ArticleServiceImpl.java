package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.Start;
import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.entity.vo.ArticleListResVo;
import com.shixun7zu.entity.vo.ArticleListVo;
import com.shixun7zu.entity.vo.ImagesListVo;
import com.shixun7zu.entity.vo.PersonalArticleVo;
import com.shixun7zu.enums.AppHttpCodeEnum;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.mapper.ArticleMapper;
import com.shixun7zu.mapper.StartMapper;
import com.shixun7zu.service.ArticleService;
import com.shixun7zu.service.StartService;
import com.shixun7zu.util.BeanCopyUtils;
import com.shixun7zu.util.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @Resource
    private StartMapper startMapper;
    @Resource
    private StartService startService;

    /**
     * 获取文章列表
     *
     * @param num    页码
     * @param size   每页大小
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
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        List<ImagesListVo> resList = new ArrayList<>();
        articleListVos.forEach(articleListVo -> {
            ImagesListVo imagesListVo = BeanCopyUtils.copyBean(articleListVo, ImagesListVo.class);
            imagesListVo.setArticleImages(Arrays.asList(articleListVo.getArticleImages().split(",")));
            if (imagesListVo.getArticleImages().get(0).equals("")) imagesListVo.setArticleImages(null);
            resList.add(imagesListVo);
        });
        return ResponseResult.okResult(resList);
    }
    /**
     * 获取文章列表(登陆后)
     */
    @Override
    public ResponseResult<?> getArticleListAfterLogin(Integer num, Integer size, Integer status) {
        String username = SecurityUtils.getUsername();
        Account account = accountMapper.selectOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getUsername, username));
        Integer id = account.getId();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, status)
                .orderByDesc(Article::getCreateTime);
//        List<Article> articleList = list(queryWrapper);
//        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);
        IPage<Article> page = articleMapper.selectPage(new Page<>(num, size), queryWrapper);
        List<ArticleListResVo> resVoList = new ArrayList<>();
        List<ArticleListVo> articleListVoList = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        articleListVoList.forEach(articleListVo -> {
            ImagesListVo imagesListVo = BeanCopyUtils.copyBean(articleListVo, ImagesListVo.class);
            imagesListVo.setArticleImages(Arrays.asList(articleListVo.getArticleImages().split(",")));
            if (imagesListVo.getArticleImages().get(0).equals("")) imagesListVo.setArticleImages(null);
            if (Objects.equals(articleListVo.getAccountId(), id))
                resVoList.add(new ArticleListResVo(imagesListVo,true));
            else resVoList.add(new ArticleListResVo(imagesListVo,false));
        });
        return ResponseResult.okResult(resVoList);
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
        LambdaQueryWrapper<Account> accountLambdaQueryWrapper = new LambdaQueryWrapper<>();
        accountLambdaQueryWrapper.select(Account::getAvatar,
                        Account::getNickname,
                        Account::getId)
                .eq(Account::getUsername, SecurityUtils.getUsername());
        Account account = accountMapper.selectOne(accountLambdaQueryWrapper);
        article.setAccountId(account.getId());
        article.setAvatar(account.getAvatar());
        article.setNickname(account.getNickname());
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    public ResponseResult<?> getArticleByOwn(Integer num, Integer size) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(Article::getAccountId,
                "select id from user_account where username=" +
                        "'" + SecurityUtils.getUsername() + "'")
                .orderByDesc(Article::getCreateTime);
        IPage<Article> page = articleMapper.selectPage(new Page<>(num, size), queryWrapper);
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PersonalArticleVo personalArticleVo = new PersonalArticleVo();
        List<ImagesListVo> imagesListVoList = new ArrayList<>();
        articleListVos.forEach(articleListVo -> {
            ImagesListVo imagesListVo = BeanCopyUtils.copyBean(articleListVo, ImagesListVo.class);
            imagesListVo.setArticleImages(Arrays.asList(articleListVo.getArticleImages().split(",")));
            if (imagesListVo.getArticleImages().get(0).equals("")) imagesListVo.setArticleImages(null);
            imagesListVoList.add(imagesListVo);
        });
        personalArticleVo.setArticleListVoList(imagesListVoList);
        personalArticleVo.setCount(page.getTotal());
        return ResponseResult.okResult(personalArticleVo);
    }

    @Override
    public ResponseResult<?> addStart(Integer id) {
        if (startMapper.selectOne(new LambdaQueryWrapper<Start>()
                .eq(Start::getArticleId, id)
                .eq(Start::getUsername, SecurityUtils.getUsername())) != null)
            return ResponseResult.errorResult(500, "你点尼玛呢，要不要点十次嘛，傻逼！");
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("start=start+1")
                .eq(Article::getId, id);
        if (update(updateWrapper)) {
            if (startMapper.getStart(SecurityUtils.getUsername(), id) != null)
                startMapper.disDelStart(SecurityUtils.getUsername(), id);
            else startMapper.insert(new Start(SecurityUtils.getUsername(), id));
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult<?> delStart(Integer id) {
        if (startMapper.selectOne(new LambdaQueryWrapper<Start>()
                .eq(Start::getArticleId, id)
                .eq(Start::getUsername, SecurityUtils.getUsername())) == null)
            return ResponseResult.errorResult(500, "会不会调接口啊，还没点赞呢你jb就想取消，傻逼！");
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("start=start-1")
                .eq(Article::getId, id);
        if (update(updateWrapper)) {
            startService.delStart(SecurityUtils.getUsername(), id);
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult<?> search(String text) {

        return null;
    }
}

