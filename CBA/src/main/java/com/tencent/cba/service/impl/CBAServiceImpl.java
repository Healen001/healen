package com.tencent.cba.service.impl;

import com.tencent.cba.service.CBAService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description TODO feiba的service实现
 * @Author healen
 * @Date 2019/6/3 10:39
 */
@Service
public class CBAServiceImpl implements CBAService {

    @Value("${isTest}")
    private String isTest;
    @Value("${api.gamesUrl}")
    private String gamesUrl;
    @Value("${api.test.gamesUrl}")
    private String testGamesUrl;
    @Value("${api.matchDescUrl}")
    private String matchDescUrl;
    @Value("${api.test.teamSortUrl}")
    private String testTeamSortUrl;
    @Value("${api.teamSortUrl}")
    private String teamSortUrl;
    @Value("${filePath}")
    private String filePath;
    public final Logger logger= LoggerFactory.getLogger(getClass());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd" );
    /**
    * @Description TODO 比赛赛事
    * @param startTime
    * @param endTime
    * @Return java.lang.String
    * @Author healen
    * @Date 2019/6/10 15:06
    */
    @Override
    public String games(String startTime,String endTime) {
        String currentDate = sdf.format(new Date());
        if ("1".equals(isTest)){
            logger.info("执行测试接口：======" + testGamesUrl);
            return getContent(testGamesUrl);
        }else{
            logger.info("执行正式接口：======" + gamesUrl);
            if (startTime==null||endTime==null){
                return getContent(gamesUrl+"startTime="+currentDate+"&endTime="+currentDate);
            }else {
                return getContent(gamesUrl+"startTime="+startTime+"&endTime="+endTime);
            }
        }
    }

    /**
    * @Description TODO 比赛详情
    * @param mid
    * @Return java.lang.String
    * @Author healen
    * @Date 2019/6/10 15:06
    */
    @Override
    public String matchDesc(String mid) {
        return getContent(matchDescUrl+mid);
    }

    /**
    * @Description TODO 球队排名
    * @param competitionId
    * @Return java.lang.String
    * @Author healen
    * @Date 2019/6/12 9:58
    */
    @Override
    public String teamSort(String competitionId) {
        if ("1".equals(isTest)){
            logger.info("执行测试接口：======" + testTeamSortUrl);
            return getContent(testTeamSortUrl);
        }else {
            logger.info("执行正式接口：======" + teamSortUrl);
            return getContent(teamSortUrl + competitionId);
        }
    }

    /**
    * @Description TODO 获取信息
    * @param url
    * @Return java.lang.String
    * @Author healen
    * @Date 2019/6/3 15:56
    */
    private String getContent(String url) {
        String content = "";
        //请求结果
        CloseableHttpResponse response = null;
        //实例化httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            //实例化get方法
            HttpGet httpget = new HttpGet(url);
            //执行get请求
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            logger.info(url+"接口状态码："+response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            logger.error(e.getMessage());
            //e.printStackTrace();
        }
        logger.info(url+"数据: ========="+content);
        return content;
    }
}