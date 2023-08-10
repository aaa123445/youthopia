package com.shixun7zu.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {

    private Integer id;

    //文章id
    private Integer articleId;
    //根评论id
    private Integer rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Integer toCommentUserId;
    private String toCommentNickname;
    //回复目标评论id
    private Integer toCommentId;

    private Integer createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String avatar;

    private String nickname;

    private List<CommentVo> children;

}
