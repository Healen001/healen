package com.tencent.cba.controller;

import com.tencent.cba.service.CBAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:34
 */
@RestController
@RequestMapping("basketball/")
public class CBAController {
    @Autowired
    private CBAService CBAService;

    /**
     * @param
     * @Description TODO 比赛赛事
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/6/3 14:37
     */
    @RequestMapping("games")
    public String games(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String gamesContent = CBAService.games(startTime, endTime);
        return gamesContent;
    }

    /**
     * @param
     * @Description TODO 比赛详情
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/6/3 14:42
     */
    @RequestMapping("matchDesc")
    public String matchDesc(@RequestParam String mid) {
        String matchDescContent = CBAService.matchDesc(mid);
        return matchDescContent;
    }

    /**
     * @param
     * @Description TODO 球队排名
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/6/3 14:42
     */
    @RequestMapping("teamSort")
    public String teamSort(@RequestParam String competitionId) {
        String teamSortContent = CBAService.teamSort(competitionId);
        return teamSortContent;
    }
}
