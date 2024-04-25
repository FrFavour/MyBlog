package com.my.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleDto {
    private Long id;

    private String title;

    private String content;

    private String summary;

    private Long categoryId;

    private String thumbnail;

    private String isTop;

    private String status;

    private Long viewCount;

    private String isComment;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Integer delFlag;
    private List<Long> tags;
}
