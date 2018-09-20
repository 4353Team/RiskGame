package com.company;

import org.junit.Test;

import java.util.List;

public class PlayerTest {
    @Test
    public void roll100DiceTest() {
        Player test = new Player("Tester");
        List<Dice> out = test.rollDices(100);
        assert out.size() == 100;
        Dice last = null;
        for (Dice d:out) {
            if (last == null) {
                last = d;
            } else {
                assert last.getFaceValue() >= d.getFaceValue();
                last = d;
            }
        }
    }
    @Test
    public void roll1DiceTest() {
        Player test = new Player("Tester");
        List<Dice> out = test.rollDices(1);
        assert out.size() == 1;
        out.get(0); // shouldn't return exception
    }
}
