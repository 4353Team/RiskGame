package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoBox extends JFrame {
    private Color backgrounColor;
    private Color textColor;
    private String text;
    private Location coordinate;
    private Dimension dimension;

    public Location getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Location coordinate) {
        this.coordinate = coordinate;
    }

    public Dimension getDimension() {
        return dimension;
    }



    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

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
    public boolean contains(Point p) {
        //     System.out.println("Point is +" + p.getX()+ " " + p.getY());
        if ((p.getX() >= coordinate.getX()) && (p.getX() <= (coordinate.getX()+dimension.getWidth()))) {
            if ((p.getY() >= coordinate.getY()) && (p.getY() <= (coordinate.getY()+dimension.getHeight()))){
                return true;
            }
        }
        return false;
    }





    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.setColor(backgrounColor);
        g.fillRect(coordinate.getX(),coordinate.getY(),dimension.getWidth(),dimension.getHeight());
        g.setColor(textColor);
        g.drawString(text,coordinate.getX()+(dimension.getWidth()/2)-(text.length()*3),coordinate.getY()+dimension.getHeight()/2+5);
    }
}
