package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.AddArticleDto;
import com.my.blog.domain.dto.UpdateArticleDto;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class AdminArticleController {
    @Autowired
    IArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }

    @GetMapping("/list")
    @PreAuthorize("@permissionService.hasPermission('content:article:list')")
    public ResponseResult<PageVo> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "2") Integer pageSize, String title, String summary) {
        return articleService.getAdminArticleList(pageNum, pageSize, title, summary);
    }

    @GetMapping("{id}")
    public ResponseResult<Article> getArticle(@PathVariable("id") Long id) {
        return articleService.getArticle(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto articleDto) {
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("{id}")
    public ResponseResult delArticle(@PathVariable("id") Long id) {
        return articleService.delArticle(id);
    }
}