package com.tencent.demo.opencv;

import com.tencent.demo.utils.ImageGui;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.List;
import java.util.Vector;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.*;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/11/15 9:42
 */
public class OpenCV {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        double xx1 = 0, yy1 = 0, xx2 = 0, yy2 = 0;
        double x1, y1, x2, y2;
        Mat matSrc = Imgcodecs.imread("C:\\Users\\v_rujinghe\\Desktop\\Test.jpg");
        new ImageGui(matSrc, "原图").imshow();
        Mat middle = picture_red(matSrc);
        new ImageGui(middle, "red").imshow();
        O_x1y1(middle, xx1, yy1, xx2, yy2);
        x1 = xx1;
        y1 = yy1;
        x2 = xx2;
        y2 = yy2;
        System.out.println(x1 + "==" + x2 + "===" + y1 + "==" + y2);
    }

    static void RGB2HSV(double red, double green, double blue, double hue, double saturation, double intensity) {

        double r, g, b;
        double h, s, i;

        double sum;
        double minRGB, maxRGB;
        double theta;

        r = red / 255.0;
        g = green / 255.0;
        b = blue / 255.0;

        minRGB = ((r < g) ? (r) : (g));
        minRGB = (minRGB < b) ? (minRGB) : (b);

        maxRGB = ((r > g) ? (r) : (g));
        maxRGB = (maxRGB > b) ? (maxRGB) : (b);

        sum = r + g + b;
        i = sum / 3.0;

        if (i < 0.001 || maxRGB - minRGB < 0.001) {
            h = 0.0;
            s = 0.0;
        } else {
            s = 1.0 - 3.0 * minRGB / sum;
            theta = Math.sqrt((r - g) * (r - g) + (r - b) * (g - b));
            theta = Math.acos((r - g + r - b) * 0.5 / theta);
            if (b <= g)
                h = theta;
            else
                h = 2 * Math.PI - theta;
            if (s <= 0.01)
                h = 0;
        }

        hue = (int) (h * 180 / Math.PI);
        saturation = (int) (s * 100);
        intensity = (int) (i * 100);
    }

    static Mat picture_red(Mat srcImg) {

        int width = srcImg.cols();
        int height = srcImg.rows();
        int x, y;
        double B = 0.0, G = 0.0, R = 0.0, H = 0.0, S = 0.0, V = 0.0;
        Mat vec_rgb = Mat.zeros(srcImg.size(), CV_8UC1);

        for (x = 0; x < height; x++) {
            for (y = 0; y < width; y++) {
                B = srcImg.get(x, y)[0];
                G = srcImg.get(x, y)[1];
                R = srcImg.get(x, y)[2];
                RGB2HSV(R, G, B, H, S, V);
                //红色范围，范围参考的网上。可以自己调
                if ((H >= 312 && H <= 360) && (S >= 17 && S <= 100) && (V > 18 && V < 100)) {
                    vec_rgb.get(x, y)[0] = 255;
                }
            }
        }
        return vec_rgb;


    }

    static void O_x1y1(Mat in, double x1, double y1, double x2, double y2) {
        Mat matSrc = in;
        GaussianBlur(matSrc, matSrc.clone(), new Size(5, 5), 0);//高斯滤波，除噪点

        List<MatOfPoint> contours = new Vector<>();//contours的类型，双重的vector
        //阈值
        Imgproc.threshold(matSrc, matSrc.clone(), 63, 255, THRESH_BINARY);//图像二值化
        //寻找轮廓，这里注意，findContours的输入参数要求是二值图像，二值图像的来源大致有两种，第一种用threshold，第二种用canny
        findContours(matSrc, contours, matSrc.clone(), RETR_TREE, CHAIN_APPROX_SIMPLE, new Point(0, 0));
        System.out.println(contours.size());
        /// 计算矩
        Vector<Moments> mu = new Vector<>();

        for (int i = 0; i < contours.size(); i++) {
            mu.add(moments(contours.get(i), false));
        }
        ///  计算矩中心:
        Vector<Point> mc = new Vector<>();
        for (int i = 0; i < contours.size(); i++) {
            mc.add(new Point(mu.get(i).m10 / mu.get(i).m00, mu.get(i).m01 / mu.get(i).m00));
        }

        /// 绘制轮廓
        Mat drawing = Mat.zeros(matSrc.size(), CV_8UC1);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(255);
            drawContours(drawing, contours, i, color, 2, 8);//绘制轮廓函数
            circle(drawing, mc.get(i), 4, color, -1, 8, 0);
        }
        x1 = mc.get(0).x;
        y1 = mc.get(0).y;
        x2 = mc.get(contours.size() - 1).x;
        y2 = mc.get(contours.size() - 1).y;
        System.out.println(x1 + "==" + x2 + "===" + y1 + "==" + y2);
        new ImageGui(drawing, "outImage").imshow();
    }


}
