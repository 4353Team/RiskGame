package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    public String name;
    public Card[] cards;
    public List<Dice> dice;
    private int totalInitialTroops;

    public Player(String name) {
        totalInitialTroops = 0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getTotalInitialTroops() {
        return totalInitialTroops;
    }

    public void setTotalInitialTroops(int totalInitialTroops) {
        this.totalInitialTroops = totalInitialTroops;
    }

    public List<Dice> rollDices(int numDicesToRoll) {


        dice = new ArrayList<Dice>();
        for (int i = 0 ; i < numDicesToRoll; i++) {
            Dice d = new Dice();
            dice.add(d);
        }
        Collections.sort(dice);
        return dice;
    }


}
