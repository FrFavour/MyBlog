package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.CommentMapper;
import com.my.blog.dao.UserMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Comment;
import com.my.blog.domain.vo.CommentVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.ICommentService;
import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, -1)
                    .eq(Comment::getType, type)
                    .eq(articleId!=null, Comment::getArticleId, articleId)
                    .orderByDesc(Comment::getCreateTime);

        Page<Comment> page = new Page<>(pageNum, pageSize);

        // 查询根评论
        commentMapper.selectPage(page, queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        // 取子评论
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children = getChildren(commentVo);
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        commentMapper.insert(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(CommentVo commentVo) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, commentVo.getId())
                    .orderByAsc(Comment::getCreateTime);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return toCommentVoList(comments);
    }

    private List<CommentVo> toCommentVoList(List<Comment> records) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(records, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            String nickName = userMapper.selectById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            if (commentVo.getToCommentUserId() != -1L) {
                commentVo.setToCommentUserName(userMapper.selectById(commentVo.getToCommentUserId()).getNickName());
            }
        }
        return commentVos;
    }
}
