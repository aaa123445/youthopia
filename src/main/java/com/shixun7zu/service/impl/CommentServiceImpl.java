package com.shixun7zu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun7zu.entity.Account;
import com.shixun7zu.entity.Article;
import com.shixun7zu.entity.Comment;
import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.entity.vo.CommentVo;
import com.shixun7zu.entity.vo.PageVo;
import com.shixun7zu.enums.AppHttpCodeEnum;
import com.shixun7zu.mapper.AccountMapper;
import com.shixun7zu.mapper.CommentMapper;
import com.shixun7zu.service.ArticleService;
import com.shixun7zu.service.CommentService;
import com.shixun7zu.service.UserService;
import com.shixun7zu.util.BeanCopyUtils;
import com.shixun7zu.util.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author Jc
 * @since 2023-08-09 15:23:02
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserService userService;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private ArticleService articleService;

    @Override
    public ResponseResult<?> commentList(Long articleId, Integer pageNum, Integer pageSize) {

        //先查询父级评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getRootId,-1);

        //判断评论类型
//        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getType,commentType);

        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询子评论
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        PageVo pageVo = new PageVo(commentVoList,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> addComment(Comment comment) {

        if (!StringUtils.hasText(comment.getContent())){
            throw new NullPointerException(AppHttpCodeEnum.CONTENT_NOT_NULL.toString());
        }
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUsername,SecurityUtils.getUsername());
        Account account = accountMapper.selectOne(queryWrapper);
        comment.setCreateTime(new Date());
        comment.setCreateBy(account.getId());
        save(comment);
        articleService.update(new LambdaUpdateWrapper<Article>()
                .eq(Article::getId,comment.getArticleId())
                .setSql("comments_count = comments_count + 1"));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> delById(Integer id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    //查询子评论具体实现
    private List<CommentVo> getChildren(Integer id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> list = list(queryWrapper);
        return toCommentVoList(list);
    }

    //Vo转化
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过createBy查询用户的昵称并赋值
            Account byId = userService.getById(commentVo.getCreateBy());
            commentVo.setNickname(byId.getNickname());
            commentVo.setAvatar(byId.getAvatar());
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentNickname = userService.getById(commentVo.getToCommentUserId()).getNickname();
                commentVo.setToCommentNickname(toCommentNickname);
            }
        }
        return commentVos;
    }
}

