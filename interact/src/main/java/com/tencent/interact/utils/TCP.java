package com.tencent.interact.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
* @Description TODO tcp工具类
* @Author healen
* @Date 2019/6/13 11:24
*/
@Component
public class TCP {
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());
    public String ip;
    public int port = 6100;
    public int connectTimeOut;
    public int timeOut;
    public TCP() {
        this.ip = ip;
        this.port = 6100;
    }

    public TCP(String ip, int port,int connectTimeOut,int timeOut) {
        this.ip = ip;
        this.port = port;
        this.connectTimeOut=timeOut;
        this.timeOut=timeOut;
    }

    public void sendCommands(String[] commands) {
        Socket socket = null;
        OutputStream os = null;
        try {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
            socket.connect(inetSocketAddress,Integer.valueOf( connectTimeOut));
            socket.setSoTimeout(Integer.valueOf(timeOut));
            os = socket.getOutputStream();
            for (int i = 0; i < commands.length; i++) {
                os.write(commands[i].getBytes("UTF-8"));
            }
            os.flush();
        } catch (Exception e) {
            logger.error("",e);
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != socket) {
                    socket.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public boolean sendCommand(String command) {
        logger.info("==================================="+command);
        Socket socket = new Socket();
        OutputStream os = null;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
            socket.connect(inetSocketAddress,connectTimeOut);
            socket.setSoTimeout(Integer.valueOf(timeOut));
            os = socket.getOutputStream();
            os.write(command.getBytes("UTF-8"));
            os.flush();
            return true;
        } catch (Exception e) {
            logger.error("",e);
            return false;
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != socket) {
                    socket.close();
                }
            } catch (IOException e) {
                return false;
            }
        }
    }
}
