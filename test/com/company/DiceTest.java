package com.company;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiceTest {
    @Test
    public void Dice3vs3() {
        List<Dice> attacker = new ArrayList<>();
        List<Dice> defender = new ArrayList<>();

        int attacker_maxFace = 0;
        int size = 3;
        for (int i = 0; i < size; i++) {
            attacker.add(new Dice());
            int faceValue = attacker.get(i).getFaceValue();
            if (attacker_maxFace < faceValue)
                attacker_maxFace = faceValue;
        }
        int defender_maxFace = 0;
        for (int i = 0; i < size; i++) {
            defender.add(new Dice());
            int faceValue = defender.get(i).getFaceValue();
            if (defender_maxFace < faceValue)
                defender_maxFace = faceValue;
        }
        boolean attackerWins = attacker_maxFace > defender_maxFace;
        boolean foundDice = false;
        for (int i = 0; i < size; i++) {
            int compareTo = attacker.get(i).compareTo(defender.get(i));
            if(attackerWins && compareTo > 0 && !foundDice) {
                foundDice = attacker.get(i).getFaceValue() == attacker_maxFace;
            }
            if (!attackerWins && compareTo <= 0 && !foundDice) {
                foundDice = defender.get(i).getFaceValue() == defender_maxFace;
            }
        }
        assert foundDice: "Could not find the max dice, compare to function is likely broken";
    }

    @Test
    public void testBoundsOfDice() {
        Dice tester = new Dice();
        for (int i = 0; i < 100; i++) {
            assert tester.faceValue < 7 && tester.faceValue > 0;
            tester.rollDice();
        }
    }


}
