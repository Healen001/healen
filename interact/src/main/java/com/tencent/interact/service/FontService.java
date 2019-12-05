package com.tencent.interact.service;


import com.tencent.interact.utils.Result;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:37
 */
public interface FontService {
    Result topic();

    Result reply(String tid, String page, String count, String sort, String listTyp);

    Result money(String tid, Integer num);
}
