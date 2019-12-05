package com.tencent.interact.controller;

import com.tencent.interact.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @Description TODO 内外网中转服务
 * @Author healen
 * @Date 2019/5/31 14:26
 */
@Controller
public class RedirectController {
    @Autowired
    private RedirectService redirectService;

    /**
     * @Description TODO 带参数中转
     * @param url
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/8/2 17:44
     */
    @ResponseBody
    @RequestMapping("")
    public String redirect(@RequestParam(value = "url") String url) {
        String redirectUrl = url.substring(1).replace("|","&");
        if (!redirectUrl.contains("http")){
            redirectUrl="http://"+redirectUrl;
        }
        return redirectService.redirect(redirectUrl);
    }
}
