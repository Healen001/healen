package com.tencent.interact.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/8/2 16:23
 */
public class FileUtils {
    // 日志
    public static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * @param filePath 文件夹路径
     * @param content  保存内容
     * @param fileName 文件名字
     * @Description TODO 输出到文件中
     * @Return void
     * @Author tencent
     * @Date 2019/6/3 16:49
     */
    public static void outFile(String filePath, String content, String fileName) {
        File fileDirector = new File(filePath);
        try {
            if (!fileDirector.exists()) {
                fileDirector.mkdirs();
            }
            File file = new File(fileDirector, fileName + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
            out.write(content, 0, content.length());
            out.close();
        } catch (IOException e) {
            logger.error(fileName, e);
            e.printStackTrace();
        }
    }

    /**
     * @param pathname 文件路径（包含名字）
     * @Description TODO 读取文件
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/8/2 16:51
     */
    public static String readFile(String pathname) {
//        String pathname = "input.txt";
        // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        String result = "";
        String readStr = "";
        try {
            File file = new File(pathname + ".txt");
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
            //网友推荐更加简洁的写法
            while ((result = br.readLine()) != null) {
                // 一次读入一行数据
                readStr += result;
            }
            br.close();
//            readStr = readStr.substring(0, readStr.length() - 1);
        } catch (IOException e) {
            logger.error("", e);
            e.printStackTrace();
        }
        return readStr;
    }
}
