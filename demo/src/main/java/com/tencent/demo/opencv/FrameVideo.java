package com.tencent.demo.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import static org.bytedeco.javacpp.opencv_videoio.*;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * Created by fang on 17-4-6.
 * 实现每播放一秒视频就截取一张图片保存到本地的操作
 * 没有使用ffmpeg,则只能读取avi格式视频
 * 下载google javcv.jar
 * sudo add-apt-repository ppa:kirillshkrogalev/ffmpeg-next
 * sudo apt-get update
 * sudo apt-get install ffmpeg
 * /home/fang/BigDataSoft/opencv-2.4.13/3rdparty/ffmpeg中有opencv_ffmpeg_64.dll文件
 * 不能解析视频,提示moov atom not found 是文件有错
 */
public class FrameVideo {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        run();
    }

    public static void run() {
        //读取视频文件
        VideoCapture cap = new VideoCapture("C:\\CBA\\Clip\\1.wmv");
        System.out.println(cap.isOpened());
        //判断视频是否打开
        if (cap.isOpened()) {
            //总帧数
            double frameCount = cap.get(CV_CAP_PROP_FRAME_COUNT);
            System.out.println("视频总帧数:" + frameCount);
            //帧率
            double fps = cap.get(CV_CAP_PROP_FPS);
            System.out.println("视频帧率" + fps);
            //时间长度
            double len = frameCount / fps;
            System.out.println("视频总时长:" + len);
            Double d_s = new Double(len);
            System.out.println(d_s.intValue());
            Mat frame = new Mat();
            for (int i = 0; i < d_s.intValue(); i++) {
                //设置视频的位置(单位:毫秒)
                cap.set(CV_CAP_PROP_POS_MSEC, i * 1000);
                //读取下一帧画面
                if (cap.read(frame)) {
                    System.out.println("正在保存");
                    //保存画面到本地目录
                    imwrite("D:/Image/" + i + ".jpg", frame);
                }
            }
            //关闭视频文件
            cap.release();
        }
    }
}