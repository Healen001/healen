package com.tencent.demo.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;

/**  
 * 
 * @Title: Image.java   
 * @Description: OpenCV-4.0.0 测试文件
 * @Package com.xu.test   
 * @author: xuhyacinth     
 * @date: 2019年5月7日12:13:13   
 * @version: V-1.0.0 
 * @Copyright: 2019 xuhyacinth
 *
 */
public class Image {

	static {
		//在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		blur();
		gausssianBlur();
	}

	/**
	 * OpenCV-4.0.0 均值模糊(降噪)
	 * @return: void  
	 * @date: 2019年5月7日12:16:55
	 */
	public static void blur() {
		Mat src = Imgcodecs.imread("C:\\Users\\v_rujinghe\\Desktop\\Test.jpg");
		Mat image =new Mat(src.size(),src.type());
		Imgproc.blur(src, image, new Size(3,3),new Point(-1,-1));//奇数
		HighGui.imshow("均值模糊(降噪)", image);
		HighGui.waitKey(1);
	}

	/**
	 * OpenCV-4.0.0 高斯滤波(降噪)
	 * @return: void  
	 * @date: 2019年5月7日12:16:55
	 */
	public static void gausssianBlur() {
		Mat src = Imgcodecs.imread("C:\\Users\\v_rujinghe\\Desktop\\Test.jpg");
		Mat image =new Mat(src.size(),src.type());
		Imgproc.GaussianBlur(src, image, new Size(11,11), 7,7);//奇数
		HighGui.imshow("高斯滤波(降噪)", image);
		HighGui.waitKey(1);
	}

	/**
	 * OpenCV-4.0.0  中值滤波(降噪)
	 * @return: void  
	 * @date: 2019年5月7日12:16:55
	 */
	public static void mediaBlur() {
		Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Pictures\\3.jpeg");
		Mat image =new Mat(src.size(),src.type());
		Imgproc.medianBlur(src, image, 9);
		HighGui.imshow("中值滤波(降噪)", image);
		HighGui.waitKey(1);
	}

	/**
	 * OpenCV-4.0.0  双边滤波(降噪)
	 * @return: void  
	 * @date: 2019年5月7日12:16:55
	 */
	public static void bilateralFilter() {
		Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Pictures\\3.jpeg");
		Mat image =new Mat(src.size(),src.type());
		Imgproc.bilateralFilter(src, image, 2, 150, 9);
		HighGui.imshow("双边滤波(降噪)", image);
		HighGui.waitKey(1);
	}


}