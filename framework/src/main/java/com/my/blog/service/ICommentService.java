package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Comment;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface ICommentService extends IService<Comment> {

    ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
