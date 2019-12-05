package com.tencent.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tencent.interact.domain.Option;
import com.tencent.interact.domain.ResponseDomain;
import com.tencent.interact.domain.Rocket;
import com.tencent.interact.domain.VoteDetail;
import com.tencent.interact.service.RocketService;
import com.tencent.interact.service.VoteService;
import com.tencent.interact.utils.*;
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
public class RocketServiceImpl implements RocketService {
    @Value("${api.voteUrl}")
    private String voteUrl;
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${file.location.imagePath}")
    private String imagePath;
    @Value("${file.location.imagePath}")
    private String interactPath;
    @Value("${api.rocketTotalUrl}")
    private String rocketTotalUrl;
    @Value("${api.rocketNewUrl}")
    private String rocketNewUrl;

    @Override
    public Result rocketNew(String mid, String num) {
        String data = "";
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param = new BasicNameValuePair("mid", mid);
        list.add(param);
        logger.info("执行小火箭正式接口：======" + rocketTotalUrl + "?mid=" + mid);
        JSONObject object = HttpClientUtils.httpGet(rocketTotalUrl, list);
        logger.info("正式小火箭接口数据：" + object.toJSONString());
        String total = "";
        if ("0".equals(object.getString("code"))) {
            total = object.getJSONObject("data").getString("rank");
        }
        JSONObject detailObject;
        List<NameValuePair> list2 = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("mid", mid);
        BasicNameValuePair param2 = new BasicNameValuePair("num", num);
        list2.add(param1);
        list2.add(param2);
        logger.info("执行投票正式接口：======" + rocketNewUrl + "?mid=" + mid + "&mum=" + num);
        detailObject = HttpClientUtils.httpGet(rocketNewUrl, list2);
        logger.info("正式投票接口数据：" + detailObject.toJSONString());
        if ("0".equals(detailObject.getString("code"))) {
            JSONArray jsonArray = detailObject.getJSONArray("data");
            File rocketFile = new File(imagePath + "rocket/");
            if (!rocketFile.exists()) {
                rocketFile.mkdirs();
            }
            ArrayList<Rocket> rockets = new ArrayList<>();
            data += total + "\r\n";
            int count = 0;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(jsonArray.get(i)));
                String content = jsonObject.getString("content");
                String nick = jsonObject.getString("nick");
                String vipInfo = jsonObject.getString("vipInfo");
                String icon = jsonObject.getString("icon");
                if (!"".equals(icon) && !"".equals(content) && !"".equals(nick) && !"".equals(vipInfo) && !icon.equals(null) && !content.equals(null) && !nick.equals(null) && !vipInfo.equals(null)) {
                    count += 1;
                    Rocket rocket = new Rocket();
                    rocket.setComment(content);
                    rocket.setNick(nick);
                    rocket.setVipType(vipInfo);
                    ThreadUtils.imageDownd(icon, imagePath + "rocket/" + count + ".jpg");
//                    ImageDownload.downloadPicture(icon, imagePath + "rocket/" + count + ".jpg");
//                    ImageDownload.downloadHttpClientPicture(icon, imagePath + "rocket/" + count + ".jpg");
                    rocket.setIcon("rocket/" + count + ".jpg");
                    data += nick + "\r\n";
                    data += count + ".jpg" + "\r\n";
                    data += content + "\r\n";
                    if ("普通用户".equals(vipInfo)) {
                        data += 0 + "\r\n";
                    } else if ("腾讯体育会员".equals(vipInfo)) {
                        data += 1 + "\r\n";
                    } else {
                        data += 2 + "\r\n";
                    }
                    rockets.add(rocket);
                }
            }
            FileUtils.outFile(interactPath, data.substring(0, data.length() - 1), "rocket");
            return Result.ok(new ResponseDomain(total, rockets));
        } else {
            return Result.build(201, "数据异常");
        }
    }
}