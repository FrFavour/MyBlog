package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.CategoryListDto;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult<PageVo> list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "2") Integer pageSize, CategoryListDto categoryListDto) {
        return categoryService.pageCategoryList(pageNum, pageSize, categoryListDto);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @GetMapping("{id}")
    public ResponseResult<Category> getCategory(@PathVariable("id") Long id) {
        return categoryService.getCategory(id);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("{id}")
    public ResponseResult delCategory(@PathVariable("id") Long id) {
        return categoryService.delCateogry(id);
    }
}