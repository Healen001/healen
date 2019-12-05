package com.tencent.demo;

import com.tencent.demo.utils.ReadTextUtils;
import com.tencent.demo.utils.TCP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
//        SpringApplication.run(DemoApplication.class, args);
        new GameFrame().launchFrame();
    }

}
