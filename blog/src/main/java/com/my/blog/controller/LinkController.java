package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.ILinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 友链 前端控制器
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Controller
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private ILinkService linkService;

    @GetMapping("/getAllLink")
    @ResponseBody
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
