package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.entity.Tag;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.domain.vo.TagVo;

import java.util.List;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author WH
 * @since 2024-04-23
 */
public interface ITagService extends IService<Tag> {
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult delTag(Long id);

    ResponseResult<Tag> getTag(Long id);

    ResponseResult updateTag(Tag tag);

    List<TagVo> listAllTag();
}
