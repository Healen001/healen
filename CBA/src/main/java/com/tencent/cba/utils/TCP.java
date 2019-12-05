package com.tencent.cba.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
* @Description TODO tcp工具类
* @Author healen
* @Date 2019/6/13 11:24
*/
public class TCP {
    public String ip;
    public int port = 6100;

    public TCP() {
        this.ip = ip;
        this.port = 6100;
    }

    public TCP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void sendCommands(String[] commands) {
        Socket socket = null;
        OutputStream os = null;
        try {
            socket = new Socket(ip, port);
            os = socket.getOutputStream();
            for (int i = 0; i < commands.length; i++) {
                os.write(commands[i].getBytes("UTF-8"));
            }
            os.flush();
        } catch (Exception e) {
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
        Socket socket = null;
        OutputStream os = null;
        try {
            socket = new Socket(ip, port);
            os = socket.getOutputStream();
            os.write(command.getBytes("UTF-8"));
            os.flush();
            return true;
        } catch (Exception e) {
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
