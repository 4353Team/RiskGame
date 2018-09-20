package com.company;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class
Dice implements Comparable<Dice>{
    public int faceValue; // ToDo: getter is needed and this should be private
    @Override
    public int compareTo(Dice o) {
        return this.faceValue < o.getFaceValue()? 1 : (this.faceValue > o.getFaceValue() ? -1: 0);
        // ToDo: this method is flawed, we cannot be doing this.
        // The correct way to use compareTo is return this - that
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dice))
            return  false;
        Dice d = (Dice)obj;
        return d.faceValue == d.faceValue;
    }
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
    public void render(int faceValue) {
    }
}

