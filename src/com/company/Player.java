package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    public String name;
    public Card[] cards;
    private List<Country>territories;
    public List<Dice> dice;
    private int totalInitialTroops;

    public Player(String name) {
        totalInitialTroops = 0;
        this.name = name;
        territories = new ArrayList<>();
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

    public List<Country> getTerritories() {
        return territories;
    }

    public List<Country> getTerritoriesIn(Country.Continent continent, Map map) {
        List<Country> countries = new ArrayList<Country>();
        for (Country c: map.countries
             ) {
            if (c.continent.equals(continent)) {
                countries.add(c);
            }
        }
        return countries;
    }
    public void setTerritories(List<Country> territories) {
        this.territories = territories;
    }

    public void addTerritory(Country country) {
        territories.add(country);
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
