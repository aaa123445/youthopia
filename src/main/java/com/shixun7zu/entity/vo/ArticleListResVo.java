package com.shixun7zu.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleListResVo {
//    private Integer id;
//    private Integer accountId;
//    //头像
//    private String avatar;
//    //昵称
//    private String nickname;
//    //创建时间
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private String createTime;
//    //文章详细
//    private String articleDetailed;
//    //文章图片
//    private String articleImages;
//    //转发数
//    private Integer forwardsCount;
//    //评论数
//    private Integer commentsCount;
//    //点赞数
//    private Integer start;
    private ImagesListVo imagesListVo;
    private Boolean isPersonal;
}
