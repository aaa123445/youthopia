package com.shixun7zu.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class PersonalArticleVo {
    private Long count;
    private List<ImagesListVo> articleListVoList;

}
