package com.tencent.demo.opencv;

import com.tencent.demo.utils.ImageGui;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.util.List;
import java.util.Vector;

import static org.opencv.highgui.HighGui.imshow;

@SpringBootApplication
public class OpenCvImage {

    public static void main(String[] args) {
        JLabel imageView = new JLabel();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat srcImage = Imgcodecs.imread("C:\\Users\\v_rujinghe\\Desktop\\Test.jpg",-1);
        imshow("【原图】", srcImage);
        //首先对图像进行空间的转换
        Mat grayImage = new Mat();
        Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        //对灰度图进行滤波
        Imgproc.GaussianBlur(grayImage, grayImage, new Size(3, 3), 0, 0);
        new ImageGui(grayImage, "【滤波后的图像】").imshow();
//        HighGui.imshow("【滤波后的图像】",grayImage);
//        HighGui.waitKey(1);
        //为了得到二值图像，对灰度图进行边缘检测ze
        Mat cannyImage = new Mat();
        Mat aa = new Mat();
        Imgproc.Canny(grayImage, cannyImage, 128, 255, 3);
        //在得到的二值图像中寻找轮廓
        List<MatOfPoint> contours = new Vector<>();
        Imgproc.findContours(cannyImage, contours, aa, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        //绘制轮廓
        for (int i = 0; i < (int) contours.size(); i++) {
            System.out.println("第" + i + "轮廓的面积为：" + Imgproc.contourArea(contours.get(i)));
            Imgproc.drawContours(cannyImage, contours, i, new Scalar(255), 1, 8);
        }
        cannyImage.assignTo(cannyImage, 1);
        new ImageGui(cannyImage, "【处理后的图像】").imshow();
//        HighGui.imshow("【处理后的图像】",cannyImage);
//        HighGui.waitKey(2);
        //计算轮廓的长度
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint2f point2f = new MatOfPoint2f(contours.get(i).toArray());
            RotatedRect box = Imgproc.minAreaRect(point2f);
            Point[] boxPoints = new Point[4];
            box.points(boxPoints);
            Point pointA = new Point();
            Point pointB = new Point();
            Point pointC = new Point();
            Point pointD = new Point();
            pointA.x = (boxPoints[0].x + boxPoints[1].x) * 0.5;
            pointA.y = (boxPoints[0].y + boxPoints[1].y) * 0.5;
            pointB.x = (boxPoints[1].x + boxPoints[2].x) * 0.5;
            pointB.y = (boxPoints[1].y + boxPoints[2].y) * 0.5;
            pointC.x = (boxPoints[2].x + boxPoints[3].x) * 0.5;
            pointC.y = (boxPoints[2].y + boxPoints[3].y) * 0.5;
            pointD.x = (boxPoints[3].x + boxPoints[0].x) * 0.5;
            pointD.y = (boxPoints[3].y + boxPoints[0].y) * 0.5;
            Imgproc.circle(cannyImage, pointA, 2, new Scalar(0, 0, 255));
            Imgproc.circle(cannyImage, pointB, 2, new Scalar(0, 0, 255));
            Imgproc.circle(cannyImage, pointC, 2, new Scalar(0, 0, 255));
            Imgproc.circle(cannyImage, pointD, 2, new Scalar(0, 0, 255));

            Imgproc.line(cannyImage, pointA, pointC, new Scalar(255, 0, 0));
            Imgproc.line(cannyImage, pointD, pointB, new Scalar(255, 0, 0));
            double p = (pointA.x - pointC.x) * (pointA.x - pointC.x) + (pointA.y - pointC.y) * (pointA.y - pointC.y);
            double p2 = (pointD.x - pointB.x) * (pointD.x - pointB.x) + (pointD.y - pointB.y) * (pointD.y - pointB.y);
            double dWidth = Math.sqrt(p);
            double dWidth2 = Math.sqrt(p2);
            System.out.println("距离为：" + dWidth);
            System.out.println("距离2为：" + dWidth2);
            for (int J = 0; J <= 3; J++) {
                Imgproc.line(cannyImage, boxPoints[i], boxPoints[(J + 1) % 4], new Scalar(0, 255, 0));
                Imgproc.putText(cannyImage, "" + dWidth, boxPoints[2], Imgproc.FONT_HERSHEY_COMPLEX, 0.5, new Scalar(0, 0, 255));
            }
            new ImageGui(cannyImage, "最后").imshow();
            double g_dConLength = Imgproc.arcLength(point2f, true);
            System.out.println("【用轮廓长度计算函数计算出来的第" + i + "个轮廓的长度为：】" + g_dConLength);
        }
    }
}
