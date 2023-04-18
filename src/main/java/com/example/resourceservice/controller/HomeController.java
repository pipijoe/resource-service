package com.example.resourceservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joetao
 * @date 2023/4/18
 */
@RestController
@Api(tags = "首页")
public class HomeController {
    @GetMapping("/hello")
    @ApiOperation(value = "hello")
    public String query() {
        return "hello world";
    }
}
