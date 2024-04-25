package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.CategoryDto;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "2") Integer pageSize, CategoryDto categoryDto) {
        return categoryService.pageCategoryList(pageNum, pageSize, categoryDto);
    }
}