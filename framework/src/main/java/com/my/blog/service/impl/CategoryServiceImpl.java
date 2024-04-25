package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.CategoryDto;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.ICategoryService;
import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        // 获取已发布的文章的分类id
        Set<Long> categoryIds = articles.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        // 根据分类id查询分类列表
        List<Category> categories = categoryMapper.selectBatchIds(categoryIds);

        // 封装为vo
        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        for (Category category : categories) {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            categoryVos.add(categoryVo);
        }

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list,
                CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult<PageVo> pageCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(categoryDto.getName()), Category::getName, categoryDto.getName());
        queryWrapper.eq(StringUtils.hasText(categoryDto.getStatus()), Category::getStatus, categoryDto.getStatus());

        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Category> categoryList = page.getRecords();
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);

        PageVo pageVo = new PageVo(categoryVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);

    }
}
