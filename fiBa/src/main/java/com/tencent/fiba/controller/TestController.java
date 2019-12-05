package com.tencent.fiba.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO 接口测试
 * @Author healen
 * @Date 2019/5/31 15:09
 */
@RestController
public class TestController {
    @RequestMapping("hello")
    public String hello() {
        return "hello world！";
    }

}
