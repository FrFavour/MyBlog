package com.my.blog.controller;

import com.my.blog.constant.SystemConstants;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Comment;
import com.my.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult comment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
