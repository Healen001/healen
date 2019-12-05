package com.tencent.cba;

import com.tencent.cba.utils.TCP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @Description TODO 测试
 * @Author tencent
 * @Date 2019/6/5 17:25
 */
public class Test {
    public static void main(String[] args) {
        new GameFrame().launchFrame();
        String pathName ="D:/healen/Demo/1.txt";
        String result = txtToString(pathName);
        if (result!=null&&result!=""){
            String[] resultArr = result.split(";");
            for (int i = 0; i <resultArr.length ; i++) {
                try {
                    Thread.currentThread().sleep(Long.valueOf(resultArr[i].split(":")[0]));
                    TCP tcp = new TCP("127.0.0.1", 6100);
                    tcp.sendCommand("send RENDERER*SCRIPT INVOKE run "+resultArr[i].split(":")[1]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TCP tcp = new TCP("127.0.0.1", 6100);
            tcp.sendCommand("send RENDERER*SCRIPT INVOKE hide");
        }
        System.out.println(txtToString(pathName));
        System.out.println(System.currentTimeMillis());
    }
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
    public static class GameFrame extends JFrame {
        public void launchFrame(){
            JButton button = new JButton("按钮");

            this.setSize(200, 200);
            this.setTitle("测试");
            this.getContentPane().add(button);
            this.setLocation(200, 100);
            this.setResizable(false);
            this.setVisible(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "用户单击了按钮", "INFORMATION_MESSAGE",JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
}
}
