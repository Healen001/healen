package com.tencent.interact.service;


import com.tencent.interact.utils.Result;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:37
 */
public interface GuessService {
    Result detail(String integer, String gid);

    Result winner(String gid, Integer pageNum,Integer pageSize);
}
