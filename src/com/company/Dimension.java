package com.company;

public class Dimension {
    private int height;
    private int width;
    Dimension(int inWidth, int inHeight){
        height = inHeight;
        width = inWidth;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setWidth(int width) {
        this.width = width;
    }
}
