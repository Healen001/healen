package com.tencent.interact.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description TODO 互动首页
 * @Author healen
 * @Date 2019/5/31 14:26
 */
@Controller
public class IndexController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        System.out.println("asdfsafddsaf");
        return "index";
    }
}
