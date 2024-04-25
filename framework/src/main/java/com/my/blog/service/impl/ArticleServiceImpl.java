package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.AddArticleDto;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.ArticleTag;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.ArticleListVo;
import com.my.blog.domain.vo.HotArticleVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.IArticleService;
import com.my.blog.service.IArticleTagService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1, 10);
        articleMapper.selectPage(page, queryWrapper);
        List<Article> articles = page.getRecords();

        ArrayList<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo hotArticleVo = new HotArticleVo();
            BeanUtils.copyProperties(article, hotArticleVo);
            hotArticleVos.add(hotArticleVo);
        }

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null && categoryId != 0, Article::getCategoryId, categoryId)
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getIsTop);

        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        articleMapper.selectPage(articlePage, queryWrapper);
        List<Article> articles = articlePage.getRecords();

        ArrayList<ArticleListVo> articleListVos = new ArrayList<>();
        for (Article article : articles) {
            ArticleListVo articleListVo = new ArticleListVo();
            BeanUtils.copyProperties(article, articleListVo);
            // 根据category查询categoryName
            Category category = categoryMapper.selectById(article.getCategoryId());
            String name = category.getName();
            articleListVo.setCategoryName(name);
            articleListVos.add(articleListVo);
        }

        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);

        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article, articleDetailVo);
        Category category = categoryMapper.selectById(article.getCategoryId());
        String name = category.getName();
        articleDetailVo.setCategoryName(name);

        // 去Redis取viewCount
        Integer viewCount = redisCache.getCacheMapValue("viewCount", id.toString());
        articleDetailVo.setViewCount(Long.parseLong(viewCount.toString()));
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.addCacheMapValue("viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAdminArticleList(Integer pageNum, Integer pageSize,
                                              String title, String summary) {
//设置条件查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(title != null, Article::getTitle, title)
                .like(summary != null, Article::getSummary, summary)
                .eq(Article::getStatus, 0)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);
//设置分页查询
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
//查询文章列表
        articlePage = articleMapper.selectPage(articlePage, queryWrapper);
//封装进VO
        List<ArticleListVo> adminArticleVoList =
                BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(adminArticleVoList, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Autowired
    private IArticleTagService articleTagService;

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
}
