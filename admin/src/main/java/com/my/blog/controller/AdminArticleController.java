package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/article")
public class AdminArticleController {
    @Autowired
    IArticleService articleService;

    @GetMapping("/list")
    @PreAuthorize("@permissionService.hasPermission('content:article:list')")
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize,
                                         String title, String summary) {
        ResponseResult articles = articleService.getAdminArticleList(pageNum,
                pageSize, title, summary);
        return articles;
    }
}