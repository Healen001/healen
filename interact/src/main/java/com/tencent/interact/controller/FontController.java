package com.tencent.interact.controller;

import com.tencent.interact.service.FontService;
import com.tencent.interact.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Description TODO 文字互动
 * @Author healen
 * @Date 2019/5/31 14:26
 */
@Controller
@RequestMapping("/font")
public class FontController {
    @Autowired
    private FontService fontService;

    /**
     * @param
     * @Description TODO 发帖接口
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/6/12 17:02
     */
    @RequestMapping(value = "/topic")
    @ResponseBody
    public Result topic() {
        return fontService.topic();
    }

    /**
     * @param tid
     * @param page
     * @param count
     * @param sort
     * @param listTyp
     * @Description TODO 回帖接口
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/6/12 17:02
     */
    @RequestMapping(value ="/reply")
    @ResponseBody
    public Result reply( String tid,  String page,  String count,  String sort,  String listTyp) {
//        return fontService.reply(tid, count, page, sort, listTyp);
        return fontService.reply(tid, page, count, sort, listTyp);
    }

    @RequestMapping(value = "/money")
    @ResponseBody
    public Result money(String tid, Integer num) {
        return fontService.money(tid, num);
    }
}
