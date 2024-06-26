package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.Tag;

import java.util.List;

/**
 * <p>
 * 标签 Mapper 接口
 * </p>
 *
 * @author WH
 * @since 2024-04-23
 */
public interface TagMapper extends BaseMapper<Tag> {
    List<Long> selectAllTagByArticleId(Long id);

}
