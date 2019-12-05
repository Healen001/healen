package com.tencent.interact.utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownload {
    // 日志
    public static final Logger logger = LoggerFactory.getLogger(ImageDownload.class);

    /**
     * @param urlStr
     * @param filePath
     * @Description TODO 多线程下载图片
     * @Return void
     * @Author healen
     * @Date 2019/8/20 13:58
     */
    public static boolean downloadPicture2(String urlStr, String filePath) {
        OutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            bis = new BufferedInputStream(urlConnection.getInputStream());
            byte[] bytes = new byte[1024];
            bos = new FileOutputStream(new File(filePath));
            int len = 0;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            return true;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                logger.error("", e);
                return false;
            }
        }
    }
    /**
     * @param urlStr
     * @param filePath
     * @Description TODO 下载图片
     * @Return void
     * @Author healen
     * @Date 2019/8/20 13:58
     */
    public static boolean downloadPicture(String urlStr, String filePath) {
        OutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(200);
            urlConnection.setReadTimeout(500);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            bis = new BufferedInputStream(urlConnection.getInputStream());
            byte[] bytes = new byte[1024];
            bos = new FileOutputStream(new File(filePath));
            int len = 0;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            return true;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                logger.error("", e);
                return false;
            }
        }
    }

    /**
    * @Description TODO 通过httpClient下载图片
    * @param
    * @Return void
    * @Author healen
    * @Date 2019/8/21 11:06
    */
    public static void downloadHttpClientPicture(String urlStr, String filePath) {
        CloseableHttpClient httpClient = null;
        InputStream inputStream = null;
        FileOutputStream outStream = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet method = new HttpGet(urlStr);
            HttpResponse result = httpClient.execute(method);
            inputStream = result.getEntity().getContent();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inputStream);
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File(filePath);
            //创建输出流
            outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
                if (outStream != null) {
                    outStream.flush();
                    outStream.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    /**
    * @Description TODO 读取流
    * @param inStream
    * @Return byte[]
    * @Author healen
    * @Date 2019/8/21 11:06
    */
    public static byte[] readInputStream(InputStream inStream) {

        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream!=null){
                    outStream.flush();
                    outStream.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        //关闭输入流
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}