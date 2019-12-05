package com.tencent.cba.service;


/**
 * @Description TODO feiba接口
 * @Author healen
 * @Date 2019/6/3 10:37
 */
public interface CBAService {
    String games(String startTime,String endTime);

    String matchDesc(String mid);

    String teamSort(String competitionId);
}
