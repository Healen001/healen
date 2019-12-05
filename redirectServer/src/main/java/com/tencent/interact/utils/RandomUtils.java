package com.tencent.interact.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description TODO 抽奖工具类
 * @Author healen
 * @Date 2019/6/26 10:27
 */
public class RandomUtils {
    /**
    * @Description TODO 抽奖的方法
    * @param count 总数范围
    * @param n 得奖个数
    * @Return java.util.List
    * @Author healen
    * @Date 2019/6/26 11:01
    */
    public static List<Integer> getRandomNum(int count,int n) {
        // 生成 [0-n) 个不重复的随机数
        // list 用来保存这些随机数
        ArrayList<Integer> list = new ArrayList();
        Random rand = new Random();
        boolean[] bool = new boolean[count];
        int num = 0;
        for (int i = 0; i < n; i++) {
            do {
                // 如果产生的数相同继续循环
                num = rand.nextInt(count);
            } while (bool[num]);
            bool[num] = true;
            list.add(num);
        }
        return list;
    }
}
