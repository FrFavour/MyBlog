package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @GetMapping("/hotArticleList")
    @ResponseBody
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    @ResponseBody
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.getArticleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    @ResponseBody
    public ResponseResult updateViewCount(@PathVariable("id")Long id){
        return articleService.updateViewCount(id);
    }
}
