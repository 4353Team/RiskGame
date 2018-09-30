package riskgame.gameobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dice implements Comparable<Dice>{
    public Dice() {}

    protected List<Integer> diceFaces;

    public Dice roll(Integer numDiceToRoll) {
        diceFaces = new ArrayList<Integer>();
        for (int i = 0; i < numDiceToRoll; i++) {
            diceFaces.add((int)(Math.random()*6+1));
        }
        return this;
    }

    @Override
    public int compareTo(Dice other) {
            return this.getMax() - other.getMax();
    }

    private Integer getMax() {
        return Collections.max(diceFaces);
    }
}
