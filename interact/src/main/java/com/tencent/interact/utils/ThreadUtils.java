package com.tencent.interact.utils;

import com.alibaba.fastjson.support.odps.udf.CodecCheck;

import java.lang.management.GarbageCollectorMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/8/14 11:14
 */
public class ThreadUtils {

    // 线程池
    public  static ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

    /**
    * @Description TODO 图片下载
    * @param imageUrl
    * @param imagePath
    * @Return void
    * @Author healen
    * @Date 2019/8/14 11:23
    */
    public static void imageDownd(String imageUrl,String imagePath) {
        newCachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
//                ImageDownload.downloadHttpClientPicture(imageUrl, imagePath);
                boolean b = ImageDownload.downloadPicture2(imageUrl, imagePath);
            }
        });
    }
}
