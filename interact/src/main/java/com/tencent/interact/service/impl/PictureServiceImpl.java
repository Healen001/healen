package com.tencent.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.interact.domain.PictureReply;
import com.tencent.interact.domain.PictureTopic;
import com.tencent.interact.domain.ResponseDomain;
import com.tencent.interact.service.PictureService;
import com.tencent.interact.service.PictureThread;
import com.tencent.interact.utils.HttpClientUtils;
import com.tencent.interact.utils.ImageDownload;
import com.tencent.interact.utils.Result;
import com.tencent.interact.utils.ThreadUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:39
 */
@Service
public class PictureServiceImpl implements PictureService {
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
     * @Description TODO 帖子
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
            ArrayList<PictureTopic> topicList = new ArrayList<>();
            JSONArray topicArr = topicObject.getJSONObject("data").getJSONArray("topics");
            for (int i = 0; i < topicArr.size(); i++) {
                PictureTopic pictureTopic = new PictureTopic();
                pictureTopic.setId(JSON.parseObject(topicArr.get(i).toString()).getString("id"));
                pictureTopic.setTitle(JSON.parseObject(topicArr.get(i).toString()).getString("title"));
                topicList.add(pictureTopic);
            }
            return Result.ok(topicList);
        } else {
            return Result.build(topicObject.getInteger("code"), topicObject.getString("msg"));
        }
    }

    /**
     * @param
     * @return
     * @Description TODO 回帖
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
            ArrayList<PictureReply> pictureReplies = new ArrayList<>();
            for (int i = 0; i < replyArray.size(); i++) {
                JSONObject reply = JSON.parseObject(replyArray.get(i).toString());
                JSONArray content = reply.getJSONArray("content");
                if (content.size() >= 2) {
                    Boolean flag = false;
                    for (int j = 0; j < content.size(); j++) {
                        if (JSON.parseObject(content.get(j).toString()).getInteger("type") == 1) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                    if (flag) {
                        PictureReply pictureReply = new PictureReply();
                        pictureReply.setFloorNum(reply.getString("floorNum"));
                        pictureReply.setId(reply.getString("id"));
                        pictureReply.setTid(reply.getString("mid"));
                        pictureReply.setUid(reply.getJSONObject("user").getString("id"));
                        pictureReply.setName(reply.getJSONObject("user").getString("name"));
                        pictureReply.setVipFlag(reply.getJSONObject("user").getString("vipType"));
                        // 用户头像
                        File handFile = new File(imagePath + "hand/");
                        if (!handFile.exists()) {
                            handFile.mkdirs();
                        }
                        // 下载头像，并设置头像路径
                        boolean b1 = ImageDownload.downloadPicture(reply.getJSONObject("user").getString("avatar"), imagePath + "hand/" + pictureReply.getId() + ".jpg");
                        if (!b1){
                            continue;
                        }
//                        ThreadUtils.imageDownd(reply.getJSONObject("user").getString("avatar"), imagePath + "hand/" + pictureReply.getId() + ".jpg");
//                        ImageDownload.downloadHttpClientPicture(reply.getJSONObject("user").getString("avatar"), imagePath + "hand/" + pictureReply.getId() + ".jpg");
                        pictureReply.setHandUrl("hand/" + pictureReply.getId() + ".jpg");
                        // 帖子回复图片
                        File imageFile = new File(imagePath + "image/");
                        if (!imageFile.exists()) {
                            imageFile.mkdirs();
                        }
                        if ("0".equals(JSON.parseObject(content.get(0).toString()).getString("type"))) {
                            pictureReply.setContent(JSON.parseObject(content.get(0).toString()).getString("info"));
                        }
                        // 下载图片，并设置帖子回复图片路径
                        if ("1".equals(JSON.parseObject(content.get(1).toString()).getString("type"))) {
                            boolean b = ImageDownload.downloadPicture(JSON.parseObject(content.get(1).toString()).getJSONObject("image").getJSONObject("cur").getString("url"), imagePath + "image/" + pictureReply.getId() + ".jpg");
//                            ThreadUtils.imageDownd(JSON.parseObject(content.get(1).toString()).getJSONObject("image").getJSONObject("cur").getString("url"), imagePath + "image/" + pictureReply.getId() + ".jpg");
//                            ImageDownload.downloadHttpClientPicture(JSON.parseObject(content.get(1).toString()).getJSONObject("image").getJSONObject("cur").getString("url"), imagePath + "image/" + pictureReply.getId() + ".jpg");
                           if (!b){
                               continue;
                           }
                            pictureReply.setImageUrl("image/" + pictureReply.getId() + ".jpg");
                        }
                        pictureReplies.add(pictureReply);
                    }
                }
            }
            logger.info("总数：" + replyObject.getJSONObject("data").getString("total") + "===================================");
            return Result.ok(new ResponseDomain(replyObject.getJSONObject("data").getString("total"), pictureReplies));
        } else {
            return Result.build(replyObject.getInteger("code"), replyObject.getString("msg"));
        }
    }

}