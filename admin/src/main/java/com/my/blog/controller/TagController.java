package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.entity.Tag;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.domain.vo.TagVo;
import com.my.blog.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "2") Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag) {
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delTag(@PathVariable("id") Long id) {
        return tagService.delTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult<Tag> getTag(@PathVariable("id") Long id) {
        return tagService.getTag(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}