package com.tencent.fiba.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
* @Description TODO 循环调用接口
* @Author healen
* @Date 2019/6/6 11:39
*/
@Component
class FeiBaLoopCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... var1){
        Timer timer = new Timer();
        timer.runTimer();
    }
}