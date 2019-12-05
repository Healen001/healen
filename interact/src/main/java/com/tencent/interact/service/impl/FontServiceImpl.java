package com.tencent.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.interact.domain.FontReply;
import com.tencent.interact.domain.FontTopic;
import com.tencent.interact.domain.ResponseDomain;
import com.tencent.interact.service.FontService;
import com.tencent.interact.utils.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:39
 */
@Service
public class FontServiceImpl implements FontService {
    @Value("${isTest}")
    private String isTest;
    @Value("${api.test.topicUrl}")
    private String testTopicUrl;
    @Value("${api.test.replyUrl}")
    private String testReplyUrl;
    @Value("${api.topicUrl}")
    private String topicUrl;
    @Value("${api.replyUrl}")
    private String replyUrl;
    @Value("${file.location.imagePath}")
    private String imagePath;
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param
     * @return
     * @Description TODO
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/6/11 14:36
     */
    @Override
    public Result topic() {
        JSONObject topicObject;
        if ("1".equals(isTest)) {
            logger.info("执行测试接口：======" + testTopicUrl);
            topicObject = HttpClientUtils.httpGet(testTopicUrl, null);
            logger.info("测试接口数据：" + topicObject.toJSONString());
        } else {
            logger.info("执行正式接口：======" + topicUrl);
            topicObject = HttpClientUtils.httpGet(topicUrl, null);
            logger.info("正式接口数据" + topicObject.toJSONString());
        }
        if ("0".equals(topicObject.getString("code"))) {
            String publishArray = topicObject.getJSONObject("data").getString("topics");
            List<FontTopic> fontTopicList = JSON.parseArray(publishArray, FontTopic.class);
            return Result.ok(fontTopicList);
        } else {
            return Result.build(topicObject.getInteger("code"), topicObject.getString("msg"));
        }
    }

    /**
     * @param
     * @return
     * @Description TODO
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/6/11 14:49
     */
    @Override
    public Result reply(String tid, String page, String count, String sort, String listTyp) {
        JSONObject replyObject;
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("tid", tid);
        BasicNameValuePair param2 = new BasicNameValuePair("page", page);
        BasicNameValuePair param3 = new BasicNameValuePair("count", count);
        BasicNameValuePair param4 = new BasicNameValuePair("sort", sort);
        BasicNameValuePair param5 = new BasicNameValuePair("listTyp", listTyp);
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        list.add(param5);
        if ("1".equals(isTest)) {
            logger.info("执行测试接口：======" + testReplyUrl + "?tid=" + tid + "&count=" + count + "&page=" + page + "&sort=" + sort + "&listTyp=" + listTyp);
            replyObject = HttpClientUtils.httpGet(testReplyUrl, list);
            logger.info("测试接口数据：" + replyObject.toJSONString());
        } else {
            logger.info("执行正式接口：======" + replyUrl + "?tid=" + tid + "&count=" + count + "&page=" + page + "&sort=" + sort + "&listTyp=" + listTyp);
            replyObject = HttpClientUtils.httpGet(replyUrl, list);
            logger.info("正式接口数据：" + replyObject.toJSONString());
        }
        if ("0".equals(replyObject.getString("code"))) {
            JSONArray replyArray = replyObject.getJSONObject("data").getJSONArray("list");
            // 文字互动回复集合
            ArrayList<FontReply> fontReplies = new ArrayList<>();
            for (int i = 0; i < replyArray.size(); i++) {
                FontReply fontReply = new FontReply();
                JSONObject reply = JSON.parseObject(replyArray.get(i).toString());
                JSONArray content = reply.getJSONArray("content");
                if ("0".equals(JSON.parseObject(content.get(0).toString()).getString("type"))) {
                    fontReply.setContent(JSON.parseObject(content.get(0).toString()).getString("info"));
                }
                fontReply.setFloorNum(reply.getString("floorNum"));
                fontReply.setId(reply.getString("id"));
                fontReply.setTid(reply.getString("mid"));
                fontReply.setUid(reply.getJSONObject("user").getString("id"));
                fontReply.setName(reply.getJSONObject("user").getString("name"));
                fontReply.setVipFlag(reply.getJSONObject("user").getString("vipType"));
                // 用户头像
                File handFile = new File(imagePath + "hand/");
                if (!handFile.exists()) {
                    handFile.mkdirs();
                }
                // 下载头像，并设置头像路径
                boolean b = ImageDownload.downloadPicture(reply.getJSONObject("user").getString("avatar"), imagePath + "hand/" + fontReply.getId() + ".jpg");
//                ThreadUtils.imageDownd(reply.getJSONObject("user").getString("avatar"), imagePath + "hand/" + fontReply.getId() + ".jpg");
//                ImageDownload.downloadHttpClientPicture(reply.getJSONObject("user").getString("avatar"), imagePath + "hand/" + fontReply.getId() + ".jpg");
                if (!b){
                    continue;
                }
                fontReply.setHandUrl("hand/" + fontReply.getId() + ".jpg");
                fontReplies.add(fontReply);
            }
            return Result.ok(new ResponseDomain(replyObject.getJSONObject("data").getString("total"), fontReplies));
        } else {
            return Result.build(replyObject.getInteger("code"), replyObject.getString("msg"));
        }
    }

    /**
     * @param tid
     * @param num
     * @Description TODO 抽奖
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/6/26 9:41
     */
    @Override
    public Result money(String tid, Integer num) {
        JSONObject replyObject;
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param = new BasicNameValuePair("tid", tid);
        list.add(param);
        if ("1".equals(isTest)) {
            logger.info("执行测试接口：======" + testReplyUrl + "?tid=" + tid);
            replyObject = HttpClientUtils.httpGet(testReplyUrl, list);
            logger.info("测试接口数据：" + replyObject.toJSONString());
        } else {
            logger.info("执行正式接口：======" + replyUrl + "?tid=" + tid);
            replyObject = HttpClientUtils.httpGet(replyUrl, list);
            logger.info("正式接口数据：" + replyObject.toJSONString());
        }
        if ("0".equals(replyObject.getString("code"))) {
            JSONArray replyArray = replyObject.getJSONObject("data").getJSONArray("list");
            Integer total = replyObject.getJSONObject("data").getInteger("total");
            // 文字互动回复集合
            HashMap<String, FontReply> fontReplyHashMap = new HashMap<>();
            for (int i = 0; i < replyArray.size(); i++) {
                FontReply fontReply = new FontReply();
                JSONObject reply = JSON.parseObject(replyArray.get(i).toString());
                JSONArray content = reply.getJSONArray("content");
                if ("0".equals(JSON.parseObject(content.get(0).toString()).getString("type"))) {
                    fontReply.setContent(JSON.parseObject(content.get(0).toString()).getString("info"));
                }
                fontReply.setFloorNum(reply.getString("floorNum"));
                fontReply.setUid(reply.getJSONObject("user").getString("id"));
                fontReply.setName(reply.getJSONObject("user").getString("name"));
                fontReplyHashMap.put(fontReply.getUid(), fontReply);
            }
            int page = total / 20;
            List<Integer> rList = new ArrayList<>();
            if (page > 3) {
                // 多次请求直到拿完所有数据
                rList = RandomUtils.getRandomNum(page, 3);
            } else if (page == 1) {
                rList.add(1);
            } else if (page == 2) {
                rList.add(1);
                rList.add(2);
            } else {
                rList.add(1);
                rList.add(2);
                rList.add(3);
            }
            for (Integer randomNum : rList) {
                JSONObject replyObject1;
                List<NameValuePair> list1 = new LinkedList<>();
                BasicNameValuePair param1 = new BasicNameValuePair("tid", tid);
                BasicNameValuePair param2 = new BasicNameValuePair("page", String.valueOf(randomNum));
                list1.add(param1);
                list1.add(param2);
                if ("1".equals(isTest)) {
                    logger.info("执行测试接口：======" + testReplyUrl + "?tid=" + tid + "&page=" + randomNum);
                    replyObject1 = HttpClientUtils.httpGet(testReplyUrl, list1);
                    logger.info("测试接口数据：" + replyObject1.toJSONString());
                } else {
                    logger.info("执行正式接口：======" + replyUrl + "?tid=" + tid + "&page=" + randomNum);
                    replyObject1 = HttpClientUtils.httpGet(replyUrl, list1);
                    logger.info("正式接口数据：" + replyObject1.toJSONString());
                }
                if ("0".equals(replyObject1.getString("code"))) {
                    JSONArray replyArray1 = replyObject1.getJSONObject("data").getJSONArray("list");
                    // 文字互动回复集合
                    HashMap<String, FontReply> fontReplyHashMap1 = new HashMap<>();
                    for (int j = 0; j < replyArray1.size(); j++) {
                        FontReply fontReply = new FontReply();
                        JSONObject reply = JSON.parseObject(replyArray1.get(j).toString());
                        JSONArray content = reply.getJSONArray("content");
                        if ("0".equals(JSON.parseObject(content.get(0).toString()).getString("type"))) {
                            fontReply.setContent(JSON.parseObject(content.get(0).toString()).getString("info"));
                        }
                        fontReply.setFloorNum(reply.getString("floorNum"));
                        fontReply.setUid(reply.getJSONObject("user").getString("id"));
                        fontReply.setName(reply.getJSONObject("user").getString("name"));
                        fontReplyHashMap1.put(fontReply.getUid(), fontReply);
                    }
                    fontReplyHashMap.putAll(fontReplyHashMap1);
                }
            }
            logger.info(fontReplyHashMap.size() + "====================" + num);
            ArrayList<FontReply> moneyList = new ArrayList<>();
            if (num >= fontReplyHashMap.size()) {
                for (FontReply reply : fontReplyHashMap.values()) {
                    moneyList.add(reply);
                }
                return Result.build(200, "总数为" + fontReplyHashMap.size() + "不够" + num, new ResponseDomain(moneyList.size() + "", moneyList));
            } else {
                List<Integer> randomList = RandomUtils.getRandomNum(fontReplyHashMap.size(), num);
                for (int i = 0; i < randomList.size(); i++) {
                    moneyList.add((FontReply) fontReplyHashMap.values().toArray()[randomList.get(i) - 1]);
                }
                return Result.ok(new ResponseDomain(moneyList.size() + "", moneyList));
            }
        } else {
            return Result.build(replyObject.getInteger("code"), replyObject.getString("msg"));
        }
    }
}