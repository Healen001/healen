package com.tencent.demo.opencv;//提示：坐标依次打印在命令符窗口
//提示：坐标依次打印在命令符窗口
//提示：坐标依次打印在命令符窗口
//不就是监听鼠标事件吗？

import javafx.scene.text.Font;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;

/**
 * 我想建立个界面，可以加载本机中图片。
 * 加载后可以通过鼠标点击获得图片上任意点坐标。
 * 提问者： sunny929929 - 试用期 一级
 */
public class MyPicture extends JFrame implements MouseListener {
    private JLabel tipLabel;
    private JLabel tipLabel2;
    private Integer i = 0;
    private String message="";

    /**
     * main()
     */
    public static void main(String[] args) {
        MyPicture frame = new MyPicture();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * 选择路径或文件
     *
     * @return
     */
    public static File selectFilesAndDir() {
        JFileChooser jfc = new JFileChooser();
        //设置当前路径为桌面路径,否则将我的文档作为默认路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        jfc.setCurrentDirectory(fsv.getHomeDirectory());
        //JFileChooser.FILES_AND_DIRECTORIES 选择路径和文件
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //弹出的提示框的标题
        jfc.showDialog(new JLabel(), "确定");
        //用户选择的路径或文件
        File file = jfc.getSelectedFile();
        return file;
    }

    /**
     * constructor
     */
    public MyPicture() {
        setSize(800, 700);//根据要求调整大小
        setLocation(0, 0);
        setTitle("获得图片上任意点坐标");
        setResizable(false);

        Container con = getContentPane();
        // 选取文件
        File file = selectFilesAndDir();
        ImageIcon bgIcon = new ImageIcon(file.getPath());//注意图片的路径
        ImagePanel backpicPanel = new ImagePanel(bgIcon);
        backpicPanel.addMouseListener(this);
        con.add(backpicPanel, BorderLayout.CENTER);

        tipLabel = new JLabel("--------------------提示：坐标依次打印在屏幕最下边!--------------------");
        tipLabel2 = new JLabel("----提示：结果打印在屏幕最右边!----");
        con.add(tipLabel, BorderLayout.AFTER_LAST_LINE);
        con.add(tipLabel2,BorderLayout.EAST);
    }

    /**
     * 打印坐标
     */
    public void mousePressed(MouseEvent e) {
        i+=1;
        int x = e.getX();
        int y = e.getY();
        message += i + ":" + "(" + x + "," + y + ")  ";
        tipLabel.setText(message);
        tipLabel.setForeground(new Color(255));
        System.out.println(message);
        tipLabel2.setText("1324");
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

}

