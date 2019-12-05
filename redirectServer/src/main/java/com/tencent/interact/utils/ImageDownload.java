package com.tencent.interact.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownload {
    // 日志
    public static final Logger logger = LoggerFactory.getLogger(ImageDownload.class);

    public static void main(String[] args) {
        String url = "http://thirdqq.qlogo.cn/g?b=oidb&k=Uz5ARdOcicdmOibA21wuQb0Q&s=100";
        downloadPicture(url,"D:/healen/interact/");
    }

    //链接url下载图片
    public static void downloadPicture(String urlStr, String imageName) {
        try {
            URL url = new URL(urlStr);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            logger.error(imageName,e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(imageName,e);
            e.printStackTrace();
        }
    }
}