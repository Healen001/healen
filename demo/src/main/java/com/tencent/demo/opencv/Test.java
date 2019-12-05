package com.tencent.demo.opencv;

import com.tencent.demo.utils.ImageGui;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/11/26 16:37
 */
public class Test {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = Imgcodecs.imread("C:\\Users\\v_rujinghe\\Desktop\\1.png");
        Mat dst = src.clone();
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.adaptiveThreshold(dst, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 3, 3);
        java.util.List<MatOfPoint> contours = new java.util.ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dst, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        System.out.println(contours.size());
        new ImageGui(src, "test").imshow();
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(src, contours, i, new Scalar(0, 0, 0, 0), 1);
        }
        Imgcodecs.imwrite("D:\\Image\\Test.jpg", src);
    }
}
