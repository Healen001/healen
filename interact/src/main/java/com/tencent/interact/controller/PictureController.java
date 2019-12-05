package com.tencent.interact.controller;

import com.tencent.interact.service.PictureService;
import com.tencent.interact.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO 图片互动
 * @Author healen
 * @Date 2019/5/31 14:26
 */
@Controller
@RequestMapping("/picture")
public class PictureController {
    @Autowired
    private PictureService pictureService;

    /**
    * @Description TODO 发帖数据
    * @param
    * @Return com.tencent.interact.utils.Result
    * @Author healen
    * @Date 2019/6/12 17:01
    */
    @RequestMapping("/topic")
    @ResponseBody
    public Result topic() {
        return pictureService.topic();
    }

    /**
    * @Description TODO 回帖数据
    * @param tid
    * @param page
    * @param count
    * @param sort
    * @param listTyp
    * @Return com.tencent.interact.utils.Result
    * @Author healen
    * @Date 2019/6/12 17:01
    */
    @RequestMapping("/reply")
    @ResponseBody
    public Result reply(String tid, String page, String count, String sort, String listTyp){
        return pictureService.reply(tid, page, count,sort,listTyp);
//        return pictureService.reply(tid,count,page,sort,listTyp);
    }
}
