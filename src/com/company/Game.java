package com.company;

public class Game {
    public Map map;
    public Player[] players;
    public Card[] cards;
    // get players 2-6

    // initialize map
    public Game() {
        map = new Map();


        try {
            for (Country d : map.countries) {
                System.out.println(d.getName() + "'s territory");
                for (Country c : d.getNeighbors()) {
                    System.out.println(c.getName() + " " + c.getTroops() + " " + c.getOwner());
                }
                System.out.println();
            }

        } catch (Exception e) {
        }

     }
}
