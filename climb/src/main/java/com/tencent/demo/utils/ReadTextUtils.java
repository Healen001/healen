package com.tencent.demo.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/20 9:23
 */
public class ReadTextUtils {
    /**
     * @Description TODO 读取text文件
     * @param pathName
     * @Return java.lang.String
     * @Author healen
     * @Date 2019/6/19 11:05
     */
    public static String txtToString(String pathName){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathName),"gbk"));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}
