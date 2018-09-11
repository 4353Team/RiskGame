package com.company;

import javax.swing.*;
import java.awt.*;

public class InfoBox extends JFrame {
    private Color backgrounColor;
    private Color textColor;
    String text;
    public InfoBox() {
        backgrounColor = Color.white;
        textColor = Color.black;
        text = "123";
    }

    public InfoBox(Color bgColor, Color textColor, String text) {

    }
    public Color getBackgrounColor() {
        return backgrounColor;
    }

    public void setBackgrounColor(Color backgrounColor) {
        this.backgrounColor = backgrounColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.fillOval(800,800,100,100);
    }
}
