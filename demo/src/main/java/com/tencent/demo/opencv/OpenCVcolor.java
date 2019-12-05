package com.tencent.demo.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import java.util.Vector;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_UNCHANGED;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;

/**
 * @Description TODO  识别颜色
 * @Author healen
 * @Date 2019/11/15 16:13
 */
public class OpenCVcolor {
    public static void main(String[] args) {
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat matSrc = imread("C:\\Users\\v_rujinghe\\Desktop\\12.jpg", IMREAD_UNCHANGED);
        Mat matHsv = new Mat();
        Imgproc.cvtColor(matSrc, matHsv, COLOR_BGR2HSV);

        Vector<Double> colorVec = new Vector<>();
        colorVec.add(matHsv.get(0, 0)[0]);
        colorVec.add(matHsv.get(0, 0)[1]);
        colorVec.add(matHsv.get(0, 0)[2]);

        if ((colorVec.get(0) >= 0 && colorVec.get(0) <= 180)
                && (colorVec.get(1) >= 0 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 0 && colorVec.get(2) <= 46)) {
            System.out.println("黑");
        } else if ((colorVec.get(0) >= 0 && colorVec.get(0) <= 180)
                && (colorVec.get(1) >= 0 && colorVec.get(1) <= 43)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 220)) {

            System.out.println("灰");
        } else if ((colorVec.get(0) >= 0 && colorVec.get(0) <= 180)
                && (colorVec.get(1) >= 0 && colorVec.get(1) <= 30)
                && (colorVec.get(2) >= 221 && colorVec.get(2) <= 255)) {

            System.out.println("白");
        } else if (((colorVec.get(0) >= 0 && colorVec.get(0) <= 10) || (colorVec.get(0) >= 156 && colorVec.get(0) <= 180))
                && (colorVec.get(1) >= 43 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 255)) {

            System.out.println("红");

        } else if ((colorVec.get(0) >= 11 && colorVec.get(0) <= 25)
                && (colorVec.get(1) >= 43 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 255)) {

            System.out.println("橙");
        } else if ((colorVec.get(0) >= 26 && colorVec.get(0) <= 34)
                && (colorVec.get(1) >= 43 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 255)) {

            System.out.println("黄");
        } else if ((colorVec.get(0) >= 35 && colorVec.get(0) <= 77)
                && (colorVec.get(1) >= 43 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 255)) {

            System.out.println("绿");
        } else if ((colorVec.get(0) >= 78 && colorVec.get(0) <= 99)
                && (colorVec.get(1) >= 43 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 255)) {

            System.out.println("青");
        } else if ((colorVec.get(0) >= 100 && colorVec.get(0) <= 124)
                && (colorVec.get(1) >= 43 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 255)) {

            System.out.println("蓝");
        } else if ((colorVec.get(0) >= 125 && colorVec.get(0) <= 155)
                && (colorVec.get(1) >= 43 && colorVec.get(1) <= 255)
                && (colorVec.get(2) >= 46 && colorVec.get(2) <= 255)) {

            System.out.println("紫");
        } else {

            System.out.println("未知");
        }

    }

}