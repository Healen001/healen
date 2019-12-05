package com.tencent.interact;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void test(){
            Socket socket = new Socket();
            SocketAddress endpoint = new InetSocketAddress("29.212.19.201", 2132);
            long timeMillis = System.currentTimeMillis();
            try {
                socket.connect(endpoint, 10000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis()-timeMillis);
            System.out.println("end");
    }

}
