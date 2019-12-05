package com.tencent.demo.opencv;

import com.tencent.demo.utils.ImageGui;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Color {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String originalImgPath = "C:\\Users\\v_rujinghe\\Desktop\\Test.jpg";
        Mat img = Imgcodecs.imread(originalImgPath);
        Mat imgHSV = new Mat(img.rows(), img.cols(), CvType.CV_8UC1);
        //RGB->HSV
        Imgproc.cvtColor(img, imgHSV, Imgproc.COLOR_BGR2GRAY);
        Scalar minValues = new Scalar(0, 0, 0);
        Scalar maxValues = new Scalar(180, 255, 46);
        Mat mask = new Mat();
        Core.inRange(imgHSV, minValues, maxValues, mask);
        Mat blackImg = new Mat();
        Core.bitwise_and(imgHSV, imgHSV, blackImg);
        new ImageGui(blackImg, "aa").imshow();
    }
}