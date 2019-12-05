package com.tencent.interact.service;


import com.tencent.interact.utils.Result;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:37
 */
public interface VoteService {
    Result detail(String voteId);

    Result save(String voteId, String questionId, String num, String select);
}
