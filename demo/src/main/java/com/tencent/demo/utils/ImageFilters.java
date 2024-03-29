package com.tencent.demo.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageFilters {

    /**
    * @Description TODO 反色处理
    * @param image
    * @Return org.opencv.core.Mat
    * @Author healen
    * @Date 2019/11/20 16:34
    */
    public Mat inverse(Mat image) {
        int width = image.cols();
        int height = image.rows();
        int dims = image.channels();
        byte[] data = new byte[width * height * dims];
        image.get(0, 0, data);
        int index = 0;
        int r = 0, g = 0, b = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width * dims; col += dims) {
                index = row * width * dims + col;
                b = data[index] & 0xff;
                g = data[index + 1] & 0xff;
                r = data[index + 2] & 0xff;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                data[index] = (byte) b;
                data[index + 1] = (byte) g;
                data[index + 2] = (byte) r;
            }
        }
        image.put(0, 0, data);
        return image;
    }

    /**
     * @param image
     * @Description TODO 亮度提升
     * @Return org.opencv.core.Mat
     * @Author healen
     * @Date 2019/11/20 16:33
     */
    public Mat brightness(Mat image) {
        Mat dst = new Mat();
        Mat black = Mat.zeros(image.size(), image.type());
        Core.addWeighted(image, 1.2, black, 0.5, 0, dst);
        return dst;
    }

    /**
     * @param image
     * @Description TODO 亮度降低
     * @Return org.opencv.core.Mat
     * @Author healen
     * @Date 2019/11/20 16:33
     */
    public Mat darkness(Mat image) {
        Mat dst = new Mat();
        Mat black = Mat.zeros(image.size(), image.type());
        Core.addWeighted(image, 0.5, black, 0.5, 0, dst);
        return dst;
    }

    /**
     * @param image
     * @Description TODO 灰度
     * @Return org.opencv.core.Mat
     * @Author healen
     * @Date 2019/11/20 16:33
     */
    public Mat gray(Mat image) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    /**
     * @param image
     * @Description TODO 锐化
     * @Return org.opencv.core.Mat
     * @Author healen
     * @Date 2019/11/20 16:33
     */
    public Mat sharpen(Mat image) {
        Mat dst = new Mat();
        float[] sharper = new float[]{0, -1, 0, -1, 5, -1, 0, -1, 0};
        Mat operator = new Mat(3, 3, CvType.CV_32FC1);
        operator.put(0, 0, sharper);
        Imgproc.filter2D(image, dst, -1, operator);
        return dst;
    }

    /**
     * @param image
     * @Description TODO 高斯模糊
     * @Return org.opencv.core.Mat
     * @Author healen
     * @Date 2019/11/20 16:33
     */
    public Mat blur(Mat image) {
        Mat dst = new Mat();
        Imgproc.GaussianBlur(image, dst, new Size(15, 15), 0);
        return dst;
    }

    /**
     * @param image
     * @Description TODO 梯度
     * @Return org.opencv.core.Mat
     * @Author healen
     * @Date 2019/11/20 16:34
     */
    public Mat gradient(Mat image) {
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();

        Imgproc.Sobel(image, grad_x, CvType.CV_32F, 1, 0);
        Imgproc.Sobel(image, grad_y, CvType.CV_32F, 0, 1);
        Core.convertScaleAbs(grad_x, abs_grad_x);
        Core.convertScaleAbs(grad_y, abs_grad_y);
        grad_x.release();
        grad_y.release();
        Mat gradxy = new Mat();
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 10, gradxy);
        return gradxy;
    }
}