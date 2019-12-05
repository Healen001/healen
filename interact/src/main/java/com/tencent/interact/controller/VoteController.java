package com.tencent.interact.controller;

import com.tencent.interact.service.VoteService;
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
@RequestMapping("vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    /**
    * @Description TODO 投票
    * @Return java.lang.String
    * @Author healen
    * @Date 2019/7/23 11:05
    */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Result detail(@RequestParam("voteId")String voteId) {
       return voteService.detail(voteId);
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    public Result save(@RequestParam("voteId")String voteId,@RequestParam(value = "questionId")String questionId,@RequestParam(value = "num")String num,@RequestParam(value = "select")String select) {
        return voteService.save(voteId,questionId,num,select);
    }
}
