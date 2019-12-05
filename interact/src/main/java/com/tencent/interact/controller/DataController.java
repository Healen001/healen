package com.tencent.interact.controller;

import com.tencent.interact.domain.DataDomain;
import com.tencent.interact.utils.FileUtils;
import com.tencent.interact.utils.PropertyUtil;
import com.tencent.interact.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * @Description TODO 保存数据接口
 * @Author healen
 * @Date 2019/7/11 9:25
 */
@RequestMapping(value = "data")
@RestController
public class DataController {
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());
    HashMap<Integer, String> dataMap = new HashMap<Integer, String>();

    /**
     * @param type
     * @param data
     * @param txt
     * @Description TODO 保存数据
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/8/2 16:51
     */
    @RequestMapping("/save")
    public Result save(@RequestParam("type") Integer type, @RequestParam("data") String data, @RequestParam("string") String txt) {
        if (data != null && type != null) {
            try {
                txt = txt.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                txt = txt.replaceAll("\\+", "%2B");
                String text = URLDecoder.decode(txt, "utf-8");
                dataMap.put(type, data);
                logger.info("data数据：" + text);
                if (type <= 7) {
                    String filePath = PropertyUtil.getProperty("filePath");
                    FileUtils.outFile(filePath,data,type+"map");
                    FileUtils.outFile(filePath,text, type.toString());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return Result.ok();
        } else {
            return Result.build(10001, "数据为空，保存失败！");
        }
    }

    /**
     * @param type
     * @Description TODO get数据
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/8/2 16:51
     */
    @RequestMapping("/get")
    public Result get(@RequestParam("type") Integer type) {
        String data2 = dataMap.get(type);
        if (data2 != null) {
            DataDomain dataDomain = new DataDomain();
            dataDomain.settype("0");
            dataDomain.setObject(data2);
            return Result.ok(dataDomain);
        } else {
            File file = new File(PropertyUtil.getProperty("filePath") + type + "map" + ".txt");
            if (!file.exists()){
                return Result.build(10001, "数据为空");
            }else {
                String content = FileUtils.readFile(PropertyUtil.getProperty("filePath") + type+"map");
                if (content != null) {
                    DataDomain dataDomain2 = new DataDomain();
                    dataDomain2.settype("1");
                    dataDomain2.setObject(content);
                    return Result.ok(dataDomain2);
                } else {
                    return Result.build(10001, "数据为空");
                }
            }
        }
    }
}
