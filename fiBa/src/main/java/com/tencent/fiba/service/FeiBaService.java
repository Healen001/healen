package com.tencent.fiba.service;


/**
 * @Description TODO feiba接口
 * @Author healen
 * @Date 2019/6/3 10:37
 */
public interface FeiBaService {
    String games(String startTime,String endTime);

    String matchDesc(String mid);

    String teamSort(String competitionId);
}
