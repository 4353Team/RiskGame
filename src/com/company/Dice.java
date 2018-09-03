package com.company;

import java.util.Random;

public class Dice {
    public int faceValue;
    public Dice() {
        rollDice();
    }
    public void rollDice() {
        Random rand = new Random();
        int n = rand.nextInt(6)+1;
        faceValue = n;
    }
    public int getFaceValue() {
        return faceValue;
    }
}
