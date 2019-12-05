package com.tencent.demo;

import com.tencent.demo.utils.ReadTextUtils;
import com.tencent.demo.utils.TCP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
    int index;

    /**
     * @param
     * @Description TODO 制造按钮
     * @Return void
     * @Author healen
     * @Date 2019/6/20 9:41
     */
    public void launchFrame() {
        this.setLocationRelativeTo(null);//设置居中
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = new JPanel();
        this.setSize(1500, 1000);
        this.setTitle("字幕按钮");
        this.setLocation(900, 300);
        this.setResizable(false);
        this.setVisible(true);
        JButton button1 = new JButton("开启字幕");
        JButton button2 = new JButton("关闭字幕");
        JButton button = new JButton("推送");
        jp.add(button1);
        jp.add(button2);
        jp.add(button);
        this.add(jp);
        String pathName = "D:/healen/Demo/1.txt";
        String result = ReadTextUtils.txtToString(pathName);
        String[] resultArr = null;
        if (result != null && result != "") {
            resultArr = result.split(";");
        }
        pushData(button1,null);
        pushData(button2,null);
        pushData(button, resultArr);
    }

    /**
     * @param button
     * @Description TODO 字幕按钮
     * @Return void
     * @Author healen
     * @Date 2019/6/20 9:41
     */
    private void pushData(JButton button, String[] resultArr) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button.getText().equals("推送")) {
                    index++;
                    TCP tcp = new TCP("127.0.0.1", 6100);
                    if (index < resultArr.length) {
                        tcp.sendCommand("send RENDERER*SCRIPT INVOKE run " + resultArr[index].split(":")[1]);
                    } else {
                        isOpen("1");
                    }
                }else if(button.getText().equals("开启字幕")){
                    isOpen("0");
                }else{
                    isOpen("1");
                }
            }
        });
    }

    /**
     * @param flag 0:开启, 1:关闭
     * @Description TODO 是否开启字幕
     * @Return void
     * @Author healen
     * @Date 2019/6/20 17:06
     */
    private void isOpen(String flag) {
        TCP tcp = new TCP("127.0.0.1",6100);
        tcp.sendCommand("send RENDERER*SCRIPT INVOKE isOpen " + flag);
    }

}