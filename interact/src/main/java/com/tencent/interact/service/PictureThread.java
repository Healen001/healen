package com.tencent.interact.service;


import com.tencent.interact.utils.ImageDownload;

/**
 * @Description TODO 多线程，用来下载图片
 * @Author healen
 * @Date 2019/6/12 15:55
 */
public class PictureThread implements Runnable {
    // 外网图片路径
    private String imageUrl;
    // 本地存放路径
    private String imagePath;

    public PictureThread(String imageUrl, String imagePath) {
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }

    @Override
    public void run() {
        synchronized (this) {
            ImageDownload.downloadPicture(imageUrl, imagePath);
        }
    }
}
