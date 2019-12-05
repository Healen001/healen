package com.tencent.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.interact.domain.Option;
import com.tencent.interact.domain.VoteDetail;
import com.tencent.interact.service.VoteService;
import com.tencent.interact.utils.FileUtils;
import com.tencent.interact.utils.HttpClientUtils;
import com.tencent.interact.utils.PropertyUtil;
import com.tencent.interact.utils.Result;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:39
 */
@Service
public class VoteServiceImpl implements VoteService {
    @Value("${api.voteUrl}")
    private String voteUrl;
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param voteId
     * @Description TODO 竞猜详情
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/7/23 15:46
     */
    @Override
    public Result detail(String voteId) {
        JSONObject detailObject;
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("voteId", voteId);
        BasicNameValuePair param2 = new BasicNameValuePair("withPercent", "1");
        list.add(param1);
        list.add(param2);
        logger.info("执行投票正式接口：======" + voteUrl + "?voteId=" + voteId + "&withPercent=1");
        detailObject = HttpClientUtils.httpGet(voteUrl, list);
        logger.info("正式投票接口数据：" + detailObject.toJSONString());
        if ("0".equals(detailObject.getString("code"))) {
            JSONArray voteList = detailObject.getJSONObject("data").getJSONArray("questions");
            ArrayList<VoteDetail> voteDetails = new ArrayList<>();
            for (int i = 0; i < voteList.size(); i++) {
                VoteDetail voteDetail = new VoteDetail();
                voteDetail.setQuestionId(JSONObject.parseObject(voteList.get(i).toString()).getString("questionId"));
                voteDetail.setTitle(JSONObject.parseObject(voteList.get(i).toString()).getString("title"));
                voteDetail.setTotalVoteNum(JSONObject.parseObject(voteList.get(i).toString()).getString("totalVoteNum"));
                voteDetail.setStatus(JSONObject.parseObject(voteList.get(i).toString()).getString("status"));
                voteDetail.setCanRevote(JSONObject.parseObject(voteList.get(i).toString()).getString("canRevote"));
                voteDetail.setHasVote(JSONObject.parseObject(voteList.get(i).toString()).getString("hasVote"));
                voteDetail.setMyVote(JSONObject.parseObject(voteList.get(i).toString()).getString("myVote"));
                voteDetail.setOptions(JSON.parseArray(JSONObject.parseObject(voteList.get(i).toString()).getString("options"), Option.class));
                voteDetails.add(voteDetail);
            }
            return Result.ok(voteDetails);
        } else {
            return Result.build(detailObject.getInteger("code"), "数据异常！");
        }
    }

    /**
     * @param voteId
     * @param questionId
     * @param num
     * @param select
     * @Description TODO 保存投票信息
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/8/9 10:42
     */
    @Override
    public Result save(String voteId, String questionId, String num, String select) {
        Result result = detail(voteId);
        if (result.getStatus().equals(200)) {
            ArrayList<VoteDetail> voteDetails = (ArrayList<VoteDetail>) result.getData();
            VoteDetail vote = new VoteDetail();
            String voteStr = "";
            for (VoteDetail voteDetail : voteDetails) {
                if (voteDetail.getQuestionId().equals(questionId)) {
                    voteStr += voteDetail.getTitle() + "\r\n" + num + "\r\n" + select+"\r\n";
                    vote.setTitle(voteDetail.getTitle());
                    vote.setNum(num);
                    vote.setSelect(select);
                    vote.setOptions(voteDetail.getOptions());
                    List<Option> options = voteDetail.getOptions();
                    for (Option option : options) {
                        voteStr += option.getContent() + "," + option.getVoteNum() + "," + option.getPercent()+"\r\n";
                    }
                }
            }
            if (voteStr != "") {
                FileUtils.outFile(PropertyUtil.getProperty("filePath"), voteStr.substring(0,voteStr.length()-1), "7");
                return Result.ok(vote);
            } else {
                return Result.build(201, "数据为空");
            }
        } else {
            return result;
        }
    }
}