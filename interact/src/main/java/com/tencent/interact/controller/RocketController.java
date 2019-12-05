package com.tencent.interact.controller;

import com.tencent.interact.service.RocketService;
import com.tencent.interact.service.VoteService;
import com.tencent.interact.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO 竞猜接口
 * @Author healen
 * @Date 2019/5/31 14:26
 */
@Controller
@RequestMapping("rocket")
public class RocketController {

    @Autowired
    private RocketService rocketService;
    @Value("${rocket.num}")
    private String maxNum;

    /**
     * @param mid
     * @param num
     * @Description TODO 获取最新评论
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/8/14 10:23
     */
    @RequestMapping(value = "/new")
    @ResponseBody
    public Result rocketNew(@RequestParam("mid") String mid, @RequestParam(value = "num", defaultValue = "50", required = true) String num) {
        if (Integer.valueOf(num) > 200) {
            num = maxNum;
        }
        return rocketService.rocketNew(mid, num);
    }
}
