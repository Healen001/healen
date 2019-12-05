package com.tencent.cba.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cba.domain.Game;
import com.tencent.cba.utils.FileUtils;
import com.tencent.cba.utils.HttpClientUtils;
import com.tencent.cba.utils.PropertyUtil;
import com.tencent.cba.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/8/8 11:21
 */
public class Timer {
    public static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    public final Logger logger = LoggerFactory.getLogger(Timer.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    // 存放球员姓名map
    private HashMap<String, String> playerMap = new HashMap<>();
    // 那球员名称数据标识true：拿，false：不拿
    private boolean flag = true;
    // 当前日期
    private String date = "";

    // timer的执行方法
    public void runTimer() {
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    // 赛程字符串
                    String gamesString = "";
                    // 球队数据字符串
                    String teamMatch = "";
                    // 球员数据字符串
                    String playerMatch = "";

                    logger.info("定时循环任务执行次数：" + Result.getNum());
                    String currentDate = sdf.format(new Date());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    long l_date1 = 0;
                    long l_date2 = 0;
                    try {
                        l_date1 = sdf.parse(currentDate).getTime();
                        l_date2 = sdf.parse("20191101").getTime();
                    } catch (ParseException e) {
                        logger.error("", e);
                    }
                    int size = (int) ((l_date2 - l_date1) / (1000 * 60 * 60 * 24));
                    // 拿取所有的赛程
                   /* if (!date.equals(currentDate)) {
                        for (int i = 0; i < size; i++) {
                            cal.add(Calendar.DATE, 1);
                            Date d = cal.getTime();
                            String day = sdf.format(d);
                            try {
                                getAllGames(day);
                            } catch (Exception e) {
                                logger.error("", e);
                            }
                        }
                    }*/
                    // 拿取第二天以后7天的赛程
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(new Date());
                    for (int j = 0; j <= 7; j++) {
                        cal2.add(Calendar.DATE, 1);
                        Date d = cal2.getTime();
                        String day = sdf.format(d);
                        try {
                            getTomorrowGames(day);
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                    try {
                        if (date.equals(currentDate)) {
                            if (playerMap.size() != 0) {
                                flag = false;
                            } else {
                                flag = true;
                            }
                        } else {
                            flag = true;
                            date = currentDate;
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                    JSONObject dataObject = null;
                    JSONObject dataObject2 = null;
                    try {
                        // 是否开启测试接口
                        if ("1".equals(PropertyUtil.getProperty("isTest"))) {
                            logger.info("执行测试赛程接口" + PropertyUtil.getProperty("api.test.gamesUrl"));
                            dataObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test.gamesUrl"));
                            logger.info("赛程测试接口数据：" + dataObject);
                        } else if ("2".equals(PropertyUtil.getProperty("isTest"))) {
                            logger.info("执行测试赛程接口" + PropertyUtil.getProperty("api.test2.gamesUrl"));
                            dataObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test2.gamesUrl"));
                            logger.info("赛程测试接口数据：" + dataObject);
                        } else {
                            logger.info("执行正式赛程接口" + PropertyUtil.getProperty("api.gamesUrl") + "startTime=" + currentDate + "&endTime=" + currentDate);
                            dataObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.gamesUrl") + "startTime=" + currentDate + "&endTime=" + currentDate);
                            logger.info("赛程正式接口数据：" + dataObject);
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                    if (dataObject != null) {
                        JSONArray gameArray = dataObject.getJSONArray("data");
                        if (gameArray.size() != 0) {
                            for (int i = 0; i < gameArray.size(); i++) {
                                Game game = JSON.parseObject(gameArray.get(i).toString(), Game.class);
                                try {
                                    if (isDigit(game.getAwayName())) {
                                        JSONObject teamNameObject = null;
                                        if ("2".equals(PropertyUtil.getProperty("isTest"))) {
                                            logger.info("执行测试2球队名称接口" + PropertyUtil.getProperty("api.test2.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                            teamNameObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test2.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                        } else if ("1".equals(PropertyUtil.getProperty("isTest"))) {
                                            logger.info("执行测试1球队名称接口" + PropertyUtil.getProperty("api.test.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                            teamNameObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                        } else {
                                            logger.info("执行测试球队名称接口" + PropertyUtil.getProperty("api.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                            teamNameObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                        }
                                        if (teamNameObject != null) {
                                            if ("0".equals(teamNameObject.getString("code"))) {
                                                JSONObject data = teamNameObject.getJSONObject("data");
                                                if (data != null) {
                                                    JSONObject away = JSONObject.parseObject(data.getString(game.getHomeId()));
                                                    if (away != null) {
                                                        game.setAwayName(away.getString("cnName"));
                                                    }
                                                    if (game.getAwayName() == null || "".equals(game.getAwayName())) {
                                                        game.setAwayName(game.getAwayId());
                                                    }
                                                }
                                            }
                                        }
                                        logger.info("球队名称接口数据：" + teamNameObject);
                                    }
                                } catch (Exception e) {
                                    logger.error("", e);
                                }
                                try {
                                    if (isDigit(game.getHomeName())) {
                                        JSONObject teamNameObject = null;
                                        if ("2".equals(PropertyUtil.getProperty("isTest"))) {
                                            logger.info("执行测试2球队名称接口" + PropertyUtil.getProperty("api.test2.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                            teamNameObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test2.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                        } else if ("1".equals(PropertyUtil.getProperty("isTest"))) {
                                            logger.info("执行测试1球队名称接口" + PropertyUtil.getProperty("api.test.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                            teamNameObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                        } else {
                                            logger.info("执行测试球队名称接口" + PropertyUtil.getProperty("api.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                            teamNameObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.teamNameUrl") + game.getAwayId() + "," + game.getHomeId());
                                        }
                                        if (teamNameObject != null) {
                                            if ("0".equals(teamNameObject.getString("code"))) {
                                                JSONObject data = teamNameObject.getJSONObject("data");
                                                if (data != null) {
                                                    JSONObject home = JSONObject.parseObject(data.getString(game.getAwayId()));
                                                    if (home != null) {
                                                        game.setHomeName(home.getString("cnName"));
                                                    }
                                                    if (game.getHomeName() == null || "".equals(game.getHomeName())) {
                                                        game.setHomeName(game.getHomeId());
                                                    }
                                                }
                                            }
                                        }
                                        logger.info("球队名称接口数据：" + teamNameObject);
                                    }
                                } catch (Exception e) {
                                    logger.error("", e);
                                }
                                // 获取其它数据
                                JSONObject gameObject = JSON.parseObject(gameArray.get(i).toString());
                                if (gameObject.getString("specialData") != null) {
                                    JSONObject specialData = JSON.parseObject(gameObject.getString("specialData"));
                                    // 节数得分信息
                                    String goal = "";
                                    try {
                                        goal = getGoal(game, specialData);
                                    } catch (Exception e) {
                                        logger.error("", e);
                                    }
                                    // 本场最佳数据
                                    String maxPlayer = "";
                                    try {
                                        maxPlayer = getMaxPlayer(specialData);
                                    } catch (Exception e) {
                                        logger.error("", e);
                                    }
                                    // 拼接字符串
                                    gamesString += "game" + (i + 1) + ":" + gameArray.size() + "^" + game.toString() + ";goals=" + goal + ";maxPlayer=" + maxPlayer + "\r\n*";
                                }
                                // 访问比赛详情接口
                                JSONObject match = null;
                                // 投篮点数据接口
//                                JSONObject shoot = null;
                                try {
                                    if ("1".equals(PropertyUtil.getProperty("isTest"))) {
                                        logger.info("执行测试" + game.getAwayName() + "-" + game.getHomeName() + "比赛详情接口：" + PropertyUtil.getProperty("api.test.matchDescUrl") + game.getMid());
                                        match = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test.matchDescUrl") + game.getMid());
                                        logger.info(game.getAwayName() + "-" + game.getHomeName() + "测试比赛详情接口数据：" + match);
//                                        logger.info("执行测试" + game.getAwayName() + "-" + game.getHomeName() + "投篮点接口：" + PropertyUtil.getProperty("api.test.shootUrl" + game.getMid()));
//                                        shoot = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test.shootUrl") + game.getMid());
//                                        logger.info(game.getAwayName() + "-" + game.getHomeName() + "测试比赛投篮点数据：" + shoot);
                                    } else if ("2".equals(PropertyUtil.getProperty("isTest"))) {
                                        logger.info("执行测试" + game.getAwayName() + "-" + game.getHomeName() + "比赛详情接口：" + PropertyUtil.getProperty("api.test2.matchDescUrl") + game.getMid());
                                        match = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test2.matchDescUrl") + game.getMid());
                                        logger.info(game.getAwayName() + "-" + game.getHomeName() + "测试比赛详情接口数据：" + match);
//                                        logger.info("执行测试" + game.getAwayName() + "-" + game.getHomeName() + "投篮点接口：" + PropertyUtil.getProperty("api.test2.shootUrl") + game.getMid());
//                                        shoot = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test2.shootUrl") + game.getMid());
//                                        logger.info(game.getAwayName() + "-" + game.getHomeName() + "测试比赛投篮点数据：" + shoot);
                                    } else {
                                        logger.info("执行正式" + game.getAwayName() + "-" + game.getHomeName() + "比赛详情接口：" + PropertyUtil.getProperty("api.matchDescUrl") + game.getMid());
                                        match = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.matchDescUrl") + game.getMid());
                                        logger.info(game.getAwayName() + "-" + game.getHomeName() + "正式比赛详情接口数据：" + match);
//                                        logger.info("执行正式" + game.getAwayName() + "-" + game.getHomeName() + "投篮点接口：" + PropertyUtil.getProperty("api.shootUrl") + game.getMid());
//                                        shoot = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.shootUrl") + game.getMid());
//                                        logger.info(game.getAwayName() + "-" + game.getHomeName() + "测试比赛投篮点数据：" + shoot);
                                    }
                                } catch (Exception e) {
                                    logger.error("", e);
                                }
                                // 获取球员统计数据
                                if (match.getString("data").length() > 2 && match.getJSONObject("data") != null) {
                                    // 获取球员名称数据
                                    if (flag) {
                                        // 保存球员名称
                                        getPlayerName(match.getJSONObject("data"));
                                    }
                                    try {
                                        playerMatch = playerMatch + getPlayerMatch(game, match.getJSONObject("data"));
                                    } catch (Exception e) {
                                        logger.error("", e);
                                    }
                                    // 获取球队技术统计数据
                                    try {
                                        teamMatch = teamMatch + getTeamMatch(game, match.getJSONObject("data"));
                                    } catch (Exception e) {
                                        logger.error("", e);
                                    }
                                }
                                // 获取比赛投篮点数据
//                                String matchShoot = "";
//                                try {
//                                    matchShoot = getMatchShoot(shoot);
//                                } catch (Exception e) {
//                                    logger.error("", e);
//                                }
//                                if (!"".equals(matchShoot)) {
//                                    // 输出到文件中投篮点数据
//                                    outFile(matchShoot, PropertyUtil.getProperty("fileShoots") + "_" + game.getMid().replace(":", "-"));
//                                }
                            }
                            // 是否开启测试接口
                            // 球队排名
                            try {
                                if ("1".equals(PropertyUtil.getProperty("isTest"))) {
                                    logger.info("执行测试球队排名接口" + PropertyUtil.getProperty("api.test.teamSortUrl"));
                                    dataObject2 = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test.teamSortUrl"));
                                    logger.info("球队排名测试接口数据：" + dataObject);
                                } else if ("2".equals(PropertyUtil.getProperty("isTest"))) {
                                    logger.info("执行测试球队排名接口" + PropertyUtil.getProperty("api.test2.teamSortUrl"));
                                    dataObject2 = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test2.teamSortUrl"));
                                    logger.info("球队排名测试接口数据：" + dataObject);
                                } else {
                                    logger.info("执行正式球队排名接口" + PropertyUtil.getProperty("api.teamSortUrl"));
                                    dataObject2 = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.teamSortUrl"));
                                    logger.info("球队排名正式接口数据：" + dataObject);
                                }
                            } catch (Exception e) {
                                logger.error("", e);
                            }
                            if (dataObject2.getString("data").length() > 2 && dataObject2.getJSONObject("data") != null) {
                                String sortString = dataObject2.getJSONObject("data").getString("specialData");
                                logger.info("排名数据：" + sortString);
                                if (sortString != null) {
                                    String teamSort = "A=";
                                    JSONObject sortObject = JSON.parseObject(sortString);
                                    JSONArray sortObjectJSONObject = sortObject.getJSONArray("NOTAGROUP");
                                    try {
                                    for (Object o :sortObjectJSONObject) {
                                        JSONObject groupObject = JSONObject.parseObject(o.toString());
                                        teamSort += "badge:徽章-" + groupObject.getString("badge") + ",competitionId:赛事ID-" + groupObject.getString("competitionId") + ",enName:球队英文缩写-" + groupObject.getString("enName") + ",games-back:games-back-" + groupObject.getString("games-back") + ",losses:负-" + groupObject.getString("losses") + ",name:球队缩写-" + groupObject.getString("name") + ",score:积分-" + groupObject.getString("score") + ",serial:小组排名-" + groupObject.getString("serial") + ",teamId:球队ID-" + groupObject.getString("teamId") + ",teamName:球队中文名称-" + groupObject.getString("teamName") + ",wining-percentage:胜率-" + groupObject.getString("wining-percentage") + "%,wins:胜-" + groupObject.getString("wins") + ";";
                                    }
                                    } catch (Exception e2) {
                                        logger.error("", e2);
                                    }
                                    // 输出到文件中球队排名数据
                                    if (!"".equals(teamSort)) {
                                        outFile(teamSort, PropertyUtil.getProperty("fileTeamSorts"));
                                    }
                                }
                            }
                        } else {
                            logger.info("球队排名数据为空================================================================");
                        }
                        if (!"".equals(gamesString)) {
                            // 输出到文件中比赛基本信息
                            outFile(gamesString, PropertyUtil.getProperty("fileGames"));
                        }
                        if (!"".equals(teamMatch)) {
                            // 输出到文件中球员统计数据
                            outFile(playerMatch, PropertyUtil.getProperty("filePlayers"));
                        }
                        if (!"".equals(teamMatch)) {
                            // 输出到文件中球队技术统计数据
                            outFile(teamMatch, PropertyUtil.getProperty("fileMatchs"));
                        }
                    } else {
                        logger.info("赛程数据为空================================================================");
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }, 0, Integer.valueOf(PropertyUtil.getProperty("loopTime")), TimeUnit.SECONDS);
    }

    // 判断一个字符串是否都为数字
    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     *
     * @param cardNum 待检验的原始姓名
     * @return 返回是否包含
     * @author 2019年7月21日 上午9:49:40
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex=".*[a-zA-Z]+.*";
        Matcher m= Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    /**
     * @param shoot
     * @Description TODO 解析投篮点
     * @Return String
     * @Author healen
     * @Date 2019/8/9 14:28
     */
    private String getMatchShoot(JSONObject shoot) {
        String matchShoot = "";
        if (shoot.getString("code").equals("0")) {
            String dataString = shoot.getString("data");
            JSONObject data = null;
            if (dataString.length() > 2) {
                data = shoot.getJSONObject("data");
            }
            if (data != null) {
                matchShoot += data.getString("mid") + "=";
                if (data.getJSONArray("coordinates") != null && data.getJSONArray("coordinates").size() > 0) {
                    JSONArray coordinates = data.getJSONArray("coordinates");
                    for (Object object : coordinates) {
                        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(object));
                        matchShoot += jsonObject.getString("player_id") + "=";
                        JSONArray childCoordinates = jsonObject.getJSONArray("coordinates");
                        String shooting0 = "shooting:0-";
                        String shooting1 = "shooting:1-";
                        for (Object o : childCoordinates) {
                            JSONObject jsonObject1 = JSON.parseObject(JSON.toJSONString(o));
                            String shooting = jsonObject1.getString("shooting");
                            if ("1".equals(shooting)) {
                                shooting1 += "x:" + jsonObject1.getString("x") + "&y:" + jsonObject1.getString("y") + ",";
                            } else {
                                shooting0 += "x:" + jsonObject1.getString("x") + "&y:" + jsonObject1.getString("y") + ",";
                            }
                        }
                        if (shooting0.endsWith(",")) {
                            matchShoot += shooting0.substring(0, shooting0.length() - 1) + ";";
                        } else {
                            matchShoot += shooting0 + ";";
                        }
                        if (shooting1.endsWith(",")) {
                            matchShoot += shooting1.substring(0, shooting1.length() - 1) + "*";
                        } else {
                            matchShoot += shooting1 + "*";
                        }
                    }
                    matchShoot = matchShoot.substring(0, matchShoot.length() - 1);
                }
            }
        }
        return matchShoot;
    }

    /**
     * @param day
     * @Description TODO 拿取第二天的数据
     * @Return void
     * @Author healen
     * @Date 2019/8/6 18:37
     */
    private void getTomorrowGames(String day) {
        String gameStr = "";
        JSONObject dataObject;
        logger.info("执行" + day + "正式赛程接口" + PropertyUtil.getProperty("api.gamesUrl") + "startTime=" + day + "&endTime=" + day);
        dataObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.gamesUrl") + "startTime=" + day + "&endTime=" + day);
        logger.info("赛程" + day + "正式接口数据：" + dataObject);
        if (dataObject != null) {
            JSONArray gameArray = dataObject.getJSONArray("data");
            if (gameArray != null) {
                if (gameArray.size() != 0) {
                    for (int i = 0; i < gameArray.size(); i++) {
                        Game game = JSON.parseObject(gameArray.get(i).toString(), Game.class);
                        // 获取其它数据
                        JSONObject gameObject = JSON.parseObject(gameArray.get(i).toString());
                        JSONObject specialData = JSON.parseObject(gameObject.getString("specialData"));
                        if (specialData != null) {
                            // 节数得分信息
                            String goal = getGoal(game, specialData);
                            // 本场最佳数据
                            String maxPlayer = getMaxPlayer(specialData);
                            // 拼接字符串
                            gameStr += "game" + (i + 1) + ":" + gameArray.size() + "^" + game.toString() + ";goals=" + goal + ";maxPlayer=" + maxPlayer + "\r\n*";
                        }
                    }
                }
                if (gameStr != "") {
                    FileUtils.outFile(PropertyUtil.getProperty("filePath") + day, gameStr, PropertyUtil.getProperty("fileGames"));
                }
            }
        }
    }

    /**
     * @param day
     * @Description TODO 拿取今天以后的赛程
     * @Return void
     * @Author healen
     * @Date 2019/8/6 18:35
     */
    private void getAllGames(String day) {
        String gameStr = "";
        JSONObject dataObject;
        logger.info("执行正式" + day + "赛程接口" + PropertyUtil.getProperty("api.gamesUrl") + "startTime=" + day + "&endTime=" + day);
        dataObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.gamesUrl") + "startTime=" + day + "&endTime=" + day);
        logger.info("赛程正式" + day + "接口数据：" + dataObject);
        if (dataObject != null) {
            JSONArray gameArray = dataObject.getJSONArray("data");
            if (gameArray.size() != 0) {
                for (int i = 0; i < gameArray.size(); i++) {
                    Game game = JSON.parseObject(gameArray.get(i).toString(), Game.class);
                    // 获取其它数据
                    JSONObject gameObject = JSON.parseObject(gameArray.get(i).toString());
                    JSONObject specialData = JSON.parseObject(gameObject.getString("specialData"));
                    if (specialData != null) {
                        // 节数得分信息
                        String goal = getGoal(game, specialData);
                        // 本场最佳数据
                        String maxPlayer = getMaxPlayer(specialData);
                        // 拼接字符串
                        gameStr += "game" + (i + 1) + ":" + gameArray.size() + "^" + game.toString() + ";goals=" + goal + ";maxPlayer=" + maxPlayer + "\r\n*";
                    }
                }
            }
            if (gameStr != "") {
                FileUtils.outFile(PropertyUtil.getProperty("filePath") + day, gameStr, PropertyUtil.getProperty("fileGames"));
            }
        }
    }

    /**
     * @param match
     * @Description TODO 获取球员名称
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/7/11 14:50
     */
    private void getPlayerName(JSONObject match) {
        try {
            JSONObject playerMatchObject = match.getJSONObject("playerMatch");
            // 获取球员id集合
            Set<String> playerSet = playerMatchObject.keySet();
            if (playerSet.size() != 0) {
                for (String playerId : playerSet) {
                    if (playerId != "") {
                        JSONObject playerObject = null;
                        if ("1".equals(PropertyUtil.getProperty("isTest"))) {
                            logger.info("执行球员详情接口：" + PropertyUtil.getProperty("api.test.playerDescUrl") + playerId);
                            playerObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test.playerDescUrl") + playerId);
                        } else if ("2".equals(PropertyUtil.getProperty("isTest"))) {
                            logger.info("执行球员详情接口：" + PropertyUtil.getProperty("api.test2.playerDescUrl") + playerId);
                            playerObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.test2.playerDescUrl") + playerId);
                        } else {
                            logger.info("执行球员详情接口：" + PropertyUtil.getProperty("api.playerDescUrl") + playerId);
                            playerObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.playerDescUrl") + playerId);
                        }
                        logger.info("球员详情接口数据：" + playerObject);
                        if (playerObject != null) {
                            JSONObject dataObject = null;
                            if (playerObject.getString("data").length() > 2 && playerObject.getString("data") != null) {
                                dataObject = playerObject.getJSONObject("data");
                            }
                            if (null == dataObject) {
                                continue;
                            }
                            playerMap.put(playerId, dataObject.getString("cnName"));
                        }
                    }
                }
            } else {
                flag = true;
            }
        } catch (Exception e) {
            logger.error("获取球员名字", e);
        }
    }

    /**
     * @param game
     * @param match
     * @Description TODO 解析球员技术统计数据
     * @Return java.lang.String
     * @Author tencent
     * @Date 2019/6/5 16:30
     */
    private String getPlayerMatch(Game game, JSONObject match) {
        DecimalFormat df = new DecimalFormat("#.0");
        JSONObject playerMatchObject = match.getJSONObject("playerMatch");
        String playerMatch = "";
        if (playerMatchObject != null) {
            String awayID = "personId_away_" + match.getString("matchId") + "_" + match.getString("homeId") + "=";
            String homeID = "personId_home_" + match.getString("matchId") + "_" + match.getString("awayId") + "=";
            // 获取球员id集合
            Set<String> playerSet = playerMatchObject.keySet();
            if (playerSet != null && playerSet.size() > 0) {
                for (String playerId : playerSet) {
                    JSONObject player = playerMatchObject.getJSONObject(playerId);
                    if (player != null) {
                        JSONObject playerMatchSpecialData = JSON.parseObject(player.getString("SpecialData"));
                        if (playerMatchSpecialData != null) {
                            if (player.getString("TeamId").equals(match.getString("homeId"))) {
                                awayID = awayID + playerMatchSpecialData.getString("playerId") + "^";
                            }
                            if (player.getString("TeamId").equals(match.getString("awayId"))) {
                                homeID = homeID + playerMatchSpecialData.getString("playerId") + "^";
                            }
                            String freeThrowsPercent = "0.0%";
                            String fieldGoalsPercent = "0.0%";
                            String threePointsPercent = "0.0%";
                            if (playerMatchSpecialData != null) {
                                if (!"0".equals(playerMatchSpecialData.getString("freeThrows").split("/")[0])) {
                                    freeThrowsPercent = df.format(Double.valueOf(playerMatchSpecialData.getString("freeThrows").split("/")[0]) / Double.valueOf(playerMatchSpecialData.getString("freeThrows").split("/")[1]) * 100) + "%";
                                }
                                if (!"0".equals(playerMatchSpecialData.getString("fieldGoals").split("/")[0])) {
                                    fieldGoalsPercent = df.format(Double.valueOf(playerMatchSpecialData.getString("fieldGoals").split("/")[0]) / Double.valueOf(playerMatchSpecialData.getString("fieldGoals").split("/")[1]) * 100) + "%";
                                }
                                if (!"0".equals(playerMatchSpecialData.getString("threePoints").split("/")[0])) {
                                    threePointsPercent = df.format(Double.valueOf(playerMatchSpecialData.getString("threePoints").split("/")[0]) / Double.valueOf(playerMatchSpecialData.getString("threePoints").split("/")[1]) * 100) + "%";
                                }
                                String cnName = "";
                                String cName = "";
                                if (null == playerMap.get(playerId) || "".equals(playerMap.get(playerId))) {
                                    cnName = playerMatchSpecialData.getString("enName");
                                    cName = playerMatchSpecialData.getString("enName");
                                } else {
                                    cnName = playerMap.get(playerId);
                                    cName = playerMap.get(playerId);
                                }
                                if (judgeContainsStr(cName)) {
                                    if (playerId != "") {
                                        logger.info("执行球员详情接口：" + PropertyUtil.getProperty("api.playerDescUrl") + playerId);
                                        JSONObject playerObject = HttpClientUtils.httpGet(PropertyUtil.getProperty("api.playerDescUrl") + playerId);
                                        logger.info("球员详情接口数据：" + playerObject);
                                        if (playerObject != null) {
                                            JSONObject dataObject = null;
                                            if (playerObject.getString("data").length() > 2 && playerObject.getString("data") != null) {
                                                dataObject = playerObject.getJSONObject("data");
                                            }
                                            if (null == dataObject) {
                                                continue;
                                            }
                                            playerMap.put(playerId, dataObject.getString("cnName"));
                                            cnName = dataObject.getString("cnName");
                                        }
                                    }
                                }
                                if (cnName.contains("-")) {
                                    cnName = cnName.replace("-", ".");
                                }
                                playerMatch = playerMatch + game.getMid() + ":" + playerMatchSpecialData.getString("playerId") + "=playerId:" + playerMatchSpecialData.getString("playerId")
                                        + ",assists:助攻-" + playerMatchSpecialData.getString("assists")
                                        + ",blocked:盖帽-" + playerMatchSpecialData.getString("blocked")
                                        + ",defensiveRebounds:防守篮板-" + playerMatchSpecialData.getString("defensiveRebounds")
                                        + ",enName:英文名-" + playerMatchSpecialData.getString("enName")
                                        + ",cnName:中文名-" + cnName
                                        + ",fieldGoals:投篮-" + playerMatchSpecialData.getString("fieldGoals")
                                        + ",freeThrows:罚球-" + playerMatchSpecialData.getString("freeThrows")
                                        + ",minutes:时间-" + playerMatchSpecialData.getString("minutes")
                                        + ",offensiveRebounds:进攻篮板-" + playerMatchSpecialData.getString("offensiveRebounds")
                                        + ",personalFouls:犯规-" + playerMatchSpecialData.getString("personalFouls")
                                        + ",points:得分-" + playerMatchSpecialData.getString("points")
                                        + ",rebounds:篮板-" + playerMatchSpecialData.getString("rebounds")
                                        + ",started:首发-" + playerMatchSpecialData.getString("started")
                                        + ",steals:抢断-" + playerMatchSpecialData.getString("steals")
                                        + ",technicalFouls:技术犯规-" + playerMatchSpecialData.getString("technicalFouls")
                                        + ",threePoints:三分-" + playerMatchSpecialData.getString("threePoints")
                                        + ",freeThrowsPercent:罚球命中率-" + freeThrowsPercent
                                        + ",fieldGoalsPercent:投篮命中率-" + fieldGoalsPercent
                                        + ",threePointsPercent:三分命中率-" + threePointsPercent
                                        + ",turnovers:失误-" + playerMatchSpecialData.getString("turnovers") + "\r\n*";
                            }
                        }
                    }
                }
                playerMatch = playerMatch + "playerNum=" + playerSet.size() + "\r\n*";
                playerMatch = playerMatch + awayID + "\r\n*";
                playerMatch = playerMatch + homeID + "\r\n*";
            }
        }
        return playerMatch;
    }

    /**
     * @param game
     * @param match
     * @Description TODO 解析球队技术统计数据
     * @Return java.lang.String
     * @Author tencent
     * @Date 2019/6/5 15:10
     */
    private String getTeamMatch(Game game, JSONObject match) {

        DecimalFormat df = new DecimalFormat("#.0");
        JSONObject teamMatchObject = match.getJSONObject("teamMatch");
        String teamMatch = "";
        JSONObject away = teamMatchObject.getJSONObject(game.getAwayId());
        // 获取客队的技术分析数据
        if (away != null) {
            JSONObject teamAspecialData = null;
            Map specialDataMap = JSON.parseObject(away.getString("SpecialData"), Map.class);
            for (Object o : specialDataMap.values()) {
                teamAspecialData = JSON.parseObject(o.toString());
                continue;
            }
            String awayFreeThrowsPercent = "0.0%";
            String awayFieldGoalsPercent = "0.0%";
            String awayThreePointsPercent = "0.0%";
            if (teamAspecialData != null) {
                if (!"0".equals(teamAspecialData.getString("freeThrows"))) {
                    awayFreeThrowsPercent = df.format(teamAspecialData.getDouble("freeThrows") / teamAspecialData.getDouble("freeThrowsAttempted") * 100) + "%";
                }
                if (!"0".equals(teamAspecialData.getString("fieldGoals"))) {
                    awayFieldGoalsPercent = df.format(teamAspecialData.getDouble("fieldGoals") / teamAspecialData.getDouble("fieldGoalsAttempted") * 100) + "%";
                }
                if (!"0".equals(teamAspecialData.getString("threePointGoals"))) {
                    awayThreePointsPercent = df.format(teamAspecialData.getDouble("threePointGoals") / teamAspecialData.getDouble("threePointAttempted") * 100) + "%";
                }
                teamMatch = teamMatch + game.getMid() + "_" + match.getString("homeId") + "_" + match.getString("awayId") + "=awayAssist:助攻-" + teamAspecialData.getString("assists")
                        + ",awayBlocked:盖帽-" + teamAspecialData.getString("blocked")
                        + ",awayDefensiveRebounds:防守篮板-" + teamAspecialData.getString("defensiveRebounds")
                        + ",awayFieldGoals:投中次数-" + teamAspecialData.getString("fieldGoals")
                        + ",awayFieldGoalsAttempted:出手次数-" + teamAspecialData.getString("fieldGoalsAttempted")
                        + ",awayFreeThrows:罚中次数-" + teamAspecialData.getString("freeThrows")
                        + ",awayFreeThrowsAttempted:罚球次数-" + teamAspecialData.getString("freeThrowsAttempted")
                        + ",awayOffensiveRebounds:进攻篮板-" + teamAspecialData.getString("offensiveRebounds")
                        + ",awayPersonalFouls:犯规-" + teamAspecialData.getString("personalFouls")
                        + ",awayRebounds:篮板-" + teamAspecialData.getString("rebounds")
                        + ",awaySteals:抢断-" + teamAspecialData.getString("steals")
                        + ",awayThreePointAttempted:三分出手次数-" + teamAspecialData.getString("threePointAttempted")
                        + ",awayThreePointGoals:三分投中次数-" + teamAspecialData.getString("threePointGoals")
                        + ",awayTurnovers:失误-" + teamAspecialData.getString("turnovers")
                        + ",awayPenaltyShot:罚球-" + teamAspecialData.getString("freeThrows") + "/" + teamAspecialData.getString("freeThrowsAttempted")
                        + ",awayShoot:投篮-" + teamAspecialData.getString("fieldGoals") + "/" + teamAspecialData.getString("fieldGoalsAttempted")
                        + ",awayThreePoints:三分-" + teamAspecialData.getString("threePointGoals") + "/" + teamAspecialData.getString("threePointAttempted")
                        + ",awayFreeThrowsPercent:罚球命中率-" + awayFreeThrowsPercent
                        + ",awayFieldGoalsPercent:投篮命中率-" + awayFieldGoalsPercent
                        + ",awayThreePointsPercent:三分命中率-" + awayThreePointsPercent
                        + "^";
            }
        }
        // 获取主队的技术分析数据
        JSONObject home = teamMatchObject.getJSONObject(game.getHomeId());
        if (home != null) {
            Map specialDataMap = JSON.parseObject(home.getString("SpecialData"), Map.class);
            JSONObject teamHspecialData = null;
            for (Object o : specialDataMap.values()) {
                teamHspecialData = JSON.parseObject(o.toString());
                continue;
            }
            String homeFreeThrowsPercent = "0.0%";
            String homeFieldGoalsPercent = "0.0%";
            String homeThreePointsPercent = "0.0%";
            if (teamHspecialData != null) {
                if (!"0".equals(teamHspecialData.getString("freeThrows"))) {
                    homeFreeThrowsPercent = df.format(teamHspecialData.getDouble("freeThrows") / teamHspecialData.getDouble("freeThrowsAttempted") * 100) + "%";
                }
                if (!"0".equals(teamHspecialData.getString("fieldGoals"))) {
                    homeFieldGoalsPercent = df.format(teamHspecialData.getDouble("fieldGoals") / teamHspecialData.getDouble("fieldGoalsAttempted") * 100) + "%";
                }
                if (!"0".equals(teamHspecialData.getString("threePointGoals"))) {
                    homeThreePointsPercent = df.format(teamHspecialData.getDouble("threePointGoals") / teamHspecialData.getDouble("threePointAttempted") * 100) + "%";
                }
                teamMatch = teamMatch + "homeAssist:助攻-" + teamHspecialData.getString("assists")
                        + ",homeBlocked:盖帽-" + teamHspecialData.getString("blocked")
                        + ",homeDefensiveRebounds:防守篮板-" + teamHspecialData.getString("defensiveRebounds")
                        + ",homeFieldGoals:投中次数-" + teamHspecialData.getString("fieldGoals")
                        + ",homeFieldGoalsAttempted:出手次数-" + teamHspecialData.getString("fieldGoalsAttempted")
                        + ",homeFreeThrows:罚中次数-" + teamHspecialData.getString("freeThrows")
                        + ",homeFreeThrowsAttempted:罚球次数-" + teamHspecialData.getString("freeThrowsAttempted")
                        + ",homeOffensiveRebounds:进攻篮板-" + teamHspecialData.getString("offensiveRebounds")
                        + ",homePersonalFouls:犯规-" + teamHspecialData.getString("personalFouls")
                        + ",homeRebounds:篮板-" + teamHspecialData.getString("rebounds")
                        + ",homeSteals:抢断-" + teamHspecialData.getString("steals")
                        + ",homeThreePointAttempted:三分出手次数-" + teamHspecialData.getString("threePointAttempted")
                        + ",homeThreePointGoals:三分投中次数-" + teamHspecialData.getString("threePointGoals")
                        + ",homePenaltyShot:罚球-" + teamHspecialData.getString("freeThrows") + "/" + teamHspecialData.getString("freeThrowsAttempted")
                        + ",homeShoot:投篮-" + teamHspecialData.getString("fieldGoals") + "/" + teamHspecialData.getString("fieldGoalsAttempted")
                        + ",homeThreePoints:三分-" + teamHspecialData.getString("threePointGoals") + "/" + teamHspecialData.getString("threePointAttempted")
                        + ",homeFreeThrowsPercent:罚球命中率-" + homeFreeThrowsPercent
                        + ",homeFieldGoalsPercent:投篮命中率-" + homeFieldGoalsPercent
                        + ",homeThreePointsPercent:三分命中率-" + homeThreePointsPercent
                        + ",homeTurnovers:失误-" + teamHspecialData.getString("turnovers") + "\r\n*";
            }
        }
        return teamMatch;
    }

    /**
     * @param game
     * @param specialData
     * @Description TODO 解析节数得分信息
     * @Return java.lang.String
     * @Author tencent
     * @Date 2019/6/5 11:43
     */
    private String getGoal(Game game, JSONObject specialData) {

        String goal = "";
        try {
            Object goals1 = specialData.get("goals");
            if (goals1 != null && goals1.toString().length() > 2) {
                JSONObject goals = specialData.getJSONObject("goals");
                if (goals != null) {
                    Map goalsMap = JSON.parseObject(goals.toJSONString(), Map.class);
                    if (goalsMap != null && goalsMap.size() > 0) {
                        for (Object o : goalsMap.keySet()) {
                            JSONObject jsonObject = goals.getJSONObject(o.toString());
                            String awayGoal = jsonObject.getString("homeGoal");
                            String homeGoal = jsonObject.getString("awayGoal");
                            goal = goal + o.toString() + ":awayGoal-" + awayGoal + ",homeGoal-" + homeGoal + "^";
                        }
                        goal = goal.substring(0, goal.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return goal;
    }

    /**
     * @param specialData
     * @Description TODO 解析本场最佳数据
     * @Return java.lang.String
     * @Author tencent
     * @Date 2019/6/5 11:42
     */
    private String getMaxPlayer(JSONObject specialData) {
        String maxPlayer = "";
        try {
            Object maxPlayers1 = specialData.get("maxPlayers");
            if (maxPlayers1 != null&&maxPlayers1.toString().length() > 2) {
                // 由于接数据那边说是反着了，需要对调一下，不是错误
                // 客队最佳
                JSONObject maxPlayers = specialData.getJSONObject("maxPlayers");
                JSONObject away = maxPlayers.getJSONObject("home");
                if (away != null) {
                    JSONArray awayAssist = away.getJSONArray("assist");
                    if (awayAssist != null && awayAssist.size() > 0) {
                        maxPlayer = maxPlayer + "awayAssist:" + awayAssist.get(0) + "&助攻-" + awayAssist.get(1) + ",";
                    }
                    JSONArray awayPoint = away.getJSONArray("point");
                    if (awayPoint != null && awayPoint.size() > 0) {
                        maxPlayer = maxPlayer + "awayPoint:" + awayPoint.get(0) + "&得分-" + awayPoint.get(1) + ",";
                    }
                    JSONArray awayRebound = away.getJSONArray("rebound");
                    if (awayRebound != null && awayRebound.size() > 0) {
                        maxPlayer = maxPlayer + "awayRebound:" + awayRebound.get(0) + "&篮板-" + awayRebound.get(1) + "^";
                    }
                }
                // 主队最佳
                JSONObject home = maxPlayers.getJSONObject("away");
                if (home != null) {
                    JSONArray homeAssist = home.getJSONArray("assist");
                    if (homeAssist != null && homeAssist.size() > 0) {
                        maxPlayer = maxPlayer + "homeAssist:" + homeAssist.get(0) + "&助攻-" + homeAssist.get(1) + ",";
                    }
                    JSONArray homePoint = home.getJSONArray("point");
                    if (homePoint != null && homePoint.size() > 0) {
                        maxPlayer = maxPlayer + "homePoint:" + homePoint.get(0) + "&得分-" + homePoint.get(1) + ",";
                    }
                    JSONArray homeRebound = home.getJSONArray("rebound");
                    if (homeRebound != null && homeRebound.size() > 0) {
                        maxPlayer = maxPlayer + "homeRebound:" + homeRebound.get(0) + "&篮板-" + homeRebound.get(1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return maxPlayer;
    }

    /**
     * @param content
     * @Description TODO 输出到文件中
     * @Return void
     * @Author tencent
     * @Date 2019/6/3 16:49
     */
    private void outFile(String content, String fileName) {
        Date date = new Date();
        String currentDate = sdf.format(date);
        String filePath = PropertyUtil.getProperty("filePath");
        File fileDirector;
        BufferedWriter out = null;
        if (PropertyUtil.getProperty("fileTeamSorts").equals(fileName)) {
            fileDirector = new File(filePath);
        } else {
            fileDirector = new File(filePath + currentDate + "/");
        }
        try {
            if (!fileDirector.exists()) {
                fileDirector.mkdirs();
            }
            File file = new File(fileDirector, fileName + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
            out.write(content, 0, content.length() - 1);
        } catch (IOException e) {
            logger.error("写入文件", e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                logger.error("写入文件", e);
            }
        }
    }
}