package com.company;

import java.util.Random;

public class Location {
    private int x;
    private int y;
    Location() {
        Random rand = new Random();
        x = rand.nextInt(1024);

        y = rand.nextInt(768);
    }
    Location(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public Location getCenter(int width, int height) {
        return new Location(x+width/2,y+height/2);
    }
}
