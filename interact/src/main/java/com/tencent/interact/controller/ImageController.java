package com.tencent.interact.controller;

import com.tencent.interact.domain.DataDomain;
import com.tencent.interact.utils.FileUtils;
import com.tencent.interact.utils.PropertyUtil;
import com.tencent.interact.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * @Description TODO 保存数据接口
 * @Author healen
 * @Date 2019/7/11 9:25
 */
@RequestMapping(value = "image")
@RestController
public class ImageController {
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${file.location.imagePath}")
    private String imagePath;

    /**
     * @param
     * @Description TODO 保存数据
     * @Return com.tencent.interact.utils.Result
     * @Author healen
     * @Date 2019/8/2 16:51
     */
    @RequestMapping("/read")
    public Result read(@RequestParam("imageUrl") String imageUrl) {
        if (imageUrl != null && imageUrl != "") {
            FileInputStream in = null;
            byte[] data = null;
            // 读取图片字节数组
            try {
                in = new FileInputStream(imagePath + imageUrl);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                logger.error("", e);
                e.printStackTrace();
            }
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            String url = encoder.encode(data);// 返回Base64编码过的字节数组字符串
            return Result.ok("data:image/jpg;base64," + url);
        } else {
            return Result.build(201, "请检查传入的图片路径是否有值！");
        }

    }

}
