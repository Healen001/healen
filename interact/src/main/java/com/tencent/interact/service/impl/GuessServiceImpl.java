package com.tencent.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.interact.domain.GuessDetail;
import com.tencent.interact.domain.GuessDetailDomain;
import com.tencent.interact.domain.Opreation;
import com.tencent.interact.domain.Winner;
import com.tencent.interact.service.GuessService;
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

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 10:39
 */
@Service
public class GuessServiceImpl implements GuessService {
    @Value("${isTest}")
    private String isTest;
    @Value("${api.test.testGuessUrl}")
    private String testGuessUrl;
    @Value("${api.test.testWinnerUrl}")
    private String testWinnerUrl;
    @Value("${api.guessUrl}")
    private String guessUrl;
    @Value("${api.winnerUrl}")
    private String winnerUrl;
    @Value("${file.location.imagePath}")
    private String interactPath;
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param gid
     * @Description TODO 竞猜详情
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/7/23 15:46
     */
    @Override
    public Result detail(String gid, String singleGid) {
        JSONObject detailObject;
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("gid", String.valueOf(gid));
        list.add(param1);
        if ("1".equals(isTest)) {
            logger.info("执行竞猜测试接口：======" + testGuessUrl + "?gid=" + gid);
            detailObject = HttpClientUtils.httpGet(testGuessUrl, list);
            logger.info("测试竞猜接口数据：" + detailObject.toJSONString());
        } else {
            logger.info("执行竞猜正式接口：======" + guessUrl + "?gid=" + gid);
            detailObject = HttpClientUtils.httpGet(guessUrl, list);
            logger.info("正式竞猜接口数据：" + detailObject.toJSONString());
        }
        if ("0".equals(detailObject.getString("code"))) {
            JSONArray guessList = detailObject.getJSONObject("data").getJSONArray("guessList");
            GuessDetailDomain guessDetailDomain = new GuessDetailDomain();
            ArrayList<GuessDetail> guessDetails = new ArrayList<>();
            String guess = "";
            String winnerCnt = "";
            if (singleGid.equals("0")) {
                for (int i = 0; i < guessList.size(); i++) {
                    GuessDetail guessDetail = new GuessDetail();
                    guessDetail.setId(JSONObject.parseObject(guessList.get(i).toString()).getString("singleGid"));
                    guessDetail.setTitle(JSONObject.parseObject(guessList.get(i).toString()).getString("title"));
                    guessDetail.setAnswer(JSONObject.parseObject(guessList.get(i).toString()).getString("answer"));
                    guessDetail.setoList(JSON.parseArray(JSONObject.parseObject(guessList.get(i).toString()).getString("opts"), Opreation.class));
                    guessDetails.add(guessDetail);
                    guess += guessDetail.getTitle().replace(" ", "") + "\r\n" + guessDetail.getoList().size() + "\r\n";
                    for (Opreation opreation : guessDetail.getoList()) {
                        guess += opreation.getChoiceId().replace(" ", "") + "," + opreation.getText().replace(" ", "") + "," + opreation.getCntDisplay().replace(" ", "") + "," + opreation.getRatio().replace(" ", "") + "\r\n";
                    }
                    guess += guessDetail.getAnswer().replace(" ", "") + "\r\n";
                }
            } else {
                for (int i = 0; i < guessList.size(); i++) {
                    if (singleGid.equals(JSONObject.parseObject(guessList.get(i).toString()).getString("singleGid"))) {
                        GuessDetail guessDetail = new GuessDetail();
                        guessDetail.setId(JSONObject.parseObject(guessList.get(i).toString()).getString("singleGid"));
                        guessDetail.setTitle(JSONObject.parseObject(guessList.get(i).toString()).getString("title"));
                        guessDetail.setAnswer(JSONObject.parseObject(guessList.get(i).toString()).getString("answer"));
                        guessDetail.setoList(JSON.parseArray(JSONObject.parseObject(guessList.get(i).toString()).getString("opts"), Opreation.class));
                        guessDetails.add(guessDetail);
                        guess += guessDetail.getTitle().replace(" ", "") + "\r\n" + guessDetail.getoList().size() + "\r\n";
                        for (Opreation opreation : guessDetail.getoList()) {
                            guess += opreation.getChoiceId().replace(" ", "") + "," + opreation.getText().replace(" ", "") + "," + opreation.getCntDisplay().replace(" ", "") + "," + opreation.getRatio().replace(" ", "") + "\r\n";
                        }
                        guess += guessDetail.getAnswer().replace(" ", "") + "\r\n";
                    }
                }
            }
            String filePath = PropertyUtil.getProperty("filePath");
            // 输出文件Guess.txt中
            FileUtils.outFile(filePath + "winner", guess, "Guess");
            // 设置竞猜详情集合
            guessDetailDomain.setGuessDetailList(guessDetails);
            // 设置竞猜中奖人数
            guessDetailDomain.setWinnerNum(detailObject.getJSONObject("data").getString("winnerNum"));
            // 设置中奖金额
            guessDetailDomain.setAmountAvg(detailObject.getJSONObject("data").getString("amountAvg"));
            winnerCnt += guessDetailDomain.getWinnerNum().replace(" ", "") + "\r\n" + guessDetailDomain.getAmountAvg().replace(" ", "");
            // 输出到Winner.txt中
            FileUtils.outFile(filePath + "winner", winnerCnt, "WinnerCnt");
            return Result.ok(guessDetailDomain);
        } else {
            return Result.build(detailObject.getInteger("code"), "数据异常！");
        }
    }

    /**
     * @param gid
     * @param pageNum
     * @param pageSize
     * @Description TODO 中奖名单
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/7/23 15:46
     */
    @Override
    public Result winner(String gid, Integer pageNum, Integer pageSize) {
        List<Winner> winnerList = new ArrayList<>();
        String winner = "";
        int j = 0;
        for (; j < 5; j++) {
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair param1 = new BasicNameValuePair("gid", String.valueOf(gid));
            BasicNameValuePair param2 = new BasicNameValuePair("page", String.valueOf(j + 1));
            BasicNameValuePair param3 = new BasicNameValuePair("limit", String.valueOf(pageSize));
            list.add(param1);
            list.add(param2);
            list.add(param3);
            JSONObject winnerObject;
            if ("1".equals(isTest)) {
                logger.info("执行中奖测试接口：======" + testWinnerUrl + "?gid=" + gid + "&page=" + (j + 1) + "&limit=" + pageSize);
                winnerObject = HttpClientUtils.httpGet(testWinnerUrl, list);
                logger.info("测试中奖接口数据：" + winnerObject.toJSONString());
            } else {
                logger.info("执行中奖正式接口：======" + winnerUrl + "?gid=" + gid + "&page=" + (j + 1) + "&limit=" + pageSize);
                winnerObject = HttpClientUtils.httpGet(winnerUrl, list);
                logger.info("正式中奖接口数据：" + winnerObject.toJSONString());
            }
            if ("0".equals(winnerObject.getString("code"))) {
                List<Winner> wList = JSON.parseArray(winnerObject.getString("data"), Winner.class);
                winnerList.addAll(wList);
                if (winnerList.size() >= 200) {
                    j = 10;
                }
                if (wList.size() < 20) {
                    j = 10;
                }
            } else {
                j = 10;
            }
        }
        // 用户头像
        File winnerFile = new File(interactPath + "winner/");
        if (!winnerFile.exists()) {
            winnerFile.mkdirs();
        }
        for (int i = 0; i < winnerList.size(); i++) {
            winner += winnerList.get(i).getUserNick().replace(" ", "") + "," + winnerList.get(i).getUserVip().replace(" ", "") + "\r\n";
            ThreadUtils.imageDownd(winnerList.get(i).getUserIcon(), interactPath + "winner/winner" + i + ".jpg");
//            ImageDownload.downloadPicture(winnerList.get(i).getUserIcon(), interactPath + "winner/winner" + i + ".jpg");
//            ThreadUtils.imageDownd(winnerList.get(i).getUserIcon(), interactPath + "winner/winner" + i + ".jpg");
        }
        // 输出到Winner.txt中
        String filePath = PropertyUtil.getProperty("filePath");
        FileUtils.outFile(filePath + "winner", winner, "Winner");
        if (winnerList.size() <= 0) {
            return Result.build(201, "数据为空,请检查gid是否正确！");
        }
        return Result.ok(winnerList);
    }
}
