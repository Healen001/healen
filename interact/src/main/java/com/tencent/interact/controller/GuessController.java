package com.tencent.interact.controller;

import com.tencent.interact.service.GuessService;
import com.tencent.interact.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("guess")
public class GuessController {

    @Autowired
    private GuessService guessService;

    /**
    * @Description TODO 竞猜详情
    * @param gid=120
    * @Return java.lang.String
    * @Author healen
    * @Date 2019/7/23 11:05
    */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Result detail(@RequestParam("gid")String gid,@RequestParam(value = "singleGid",defaultValue = "0",required = true)String singleGid) {
       return guessService.detail(gid,singleGid);
    }

    /**
    * @Description TODO 竞猜中奖名单
    * @param
    * @Return java.lang.String
    * @Author healen
    * @Date 2019/7/23 11:06
    */
    @RequestMapping(value = "/winner")
    @ResponseBody
    public Result winner(@RequestParam("gid")String gid,@RequestParam(value = "pageNum",defaultValue="1",required=true) Integer pageNum,@RequestParam(value = "pageSize",defaultValue="200",required=true)Integer pageSize) {
        return guessService.winner(gid,pageNum,pageSize);
    }
}
