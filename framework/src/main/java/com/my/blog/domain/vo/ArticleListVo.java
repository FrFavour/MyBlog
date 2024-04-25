package com.my.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleListVo {
    private Long id;
    private String title;
    private Long categoryId;
    private String content;
    private String summary;
    private String categoryName;
    private String thumbnail;
    private String status;
    private String isTop;
    private Long viewCount;
    private String isComment;
    private LocalDateTime createTime;
}