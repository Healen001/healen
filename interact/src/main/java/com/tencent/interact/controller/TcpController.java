package com.tencent.interact.controller;

import com.tencent.interact.utils.Result;
import com.tencent.interact.utils.TCP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
* @Description TODO tcp接口
* @Author healen
* @Date 2019/7/11 9:25
*/
@RestController
public class TcpController {

    @Value("${tcp1.ip}")
    private String ip1;
    @Value("${tcp1.port}")
    private int port1;
    @Value("${tcp2.ip}")
    private String ip2;
    @Value("${tcp2.port}")
    private int port2;
    @Value("${tcp.timeOut}")
    private int timeOut;
    @Value("${tcp.connectTimeOut}")
    private int connectTimeOut;
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());
    @RequestMapping(value = "tcp")
    public Result tcp(@RequestParam("order")String order){
        TCP tcp1 = new TCP(ip1,port1,connectTimeOut,timeOut);
        TCP tcp2 = new TCP(ip2,port2,connectTimeOut,timeOut);
        boolean b = tcp1.sendCommand(order);
        boolean b2 = tcp2.sendCommand(order);
        if (b&&b2){
            return Result.ok();
        }else if (b&&b2==false){
            return Result.build(201,"b成功，b2失败！");
        }else if(b2&&b==false){
            return Result.build(201,"b2成功，b失败！");
        }else{
            return Result.build(201,"失败！");
        }
    }

}
