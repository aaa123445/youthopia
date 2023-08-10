package com.shixun7zu.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageVo {
    private List res;
    private Long count;
}
