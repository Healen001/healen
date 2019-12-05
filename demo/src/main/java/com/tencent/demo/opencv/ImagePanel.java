package com.tencent.demo.opencv;

import javax.swing.*;
import java.awt.*;
import java.awt.Image;

/**
 * 类ImagePanel，用于添加背景图片
 */
class ImagePanel extends JPanel {
    private Image img;

    public ImagePanel(ImageIcon imageIcon) {
        img = imageIcon.getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }

}