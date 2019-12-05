package com.tencent.interact.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tencent.interact.service.RedirectService;
import com.tencent.interact.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/8/2 15:39
 */
@Service
public class RedirectServiceImpl implements RedirectService {

    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String redirect(String url) {
        JSONObject jsonObject = HttpClientUtils.httpGet(url);
        logger.info("中转服务URL:"+url);
        logger.info("中转服务数据:"+jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }
}
