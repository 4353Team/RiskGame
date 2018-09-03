package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Awesome");
        Player navin = new Player("Navin",1);
        Country india = new Country("India",2, navin);
        System.out.println(india.getTroops());
        Dice d = new Dice();
        d.rollDice();
        System.out.println(d.getFaceValue());
        List<Dice> dices = new ArrayList<Dice>();
        int num_rolls = 10;
        System.out.println(num_rolls + " dices rolled. Values are : ");
        for (int i = 0; i < 10 ; i++) {
            Dice dd = new Dice();
            dices.add(dd);
            System.out.println(dd.getFaceValue());

        }
    }
}
