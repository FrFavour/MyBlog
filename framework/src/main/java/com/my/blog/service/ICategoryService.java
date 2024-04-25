package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.CategoryListDto;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.domain.vo.PageVo;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface ICategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult<PageVo> pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);

    ResponseResult addCategory(Category category);

    ResponseResult<Category> getCategory(Long id);

    ResponseResult updateCategory(Category category);

    ResponseResult delCateogry(Long id);
}
