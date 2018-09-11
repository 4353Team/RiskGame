package com.company;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Player {
    private Country capital;
    private Color playerColor;
    public String name;
    public List<Card> cards;
    public Location location;
    private List<Country>territories;
    public List<Dice> dice;
    private int totalInitialTroops;
    public Country getCapital() {
        return capital;
    }
    public void setCapital(Country capital) {
        this.capital = capital;
    }
    public Player(String name) {
        cards = new ArrayList<>();
        totalInitialTroops = 0;
        this.name = name;
        territories = new ArrayList<>();
    }
    public Color getPlayerColor() {
        return playerColor;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }
    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
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
    public void addToTotalInitialTroops(int newT) {
        this.totalInitialTroops += newT;
    }
    public List<Country> getTerritories() {
        return territories;
    }
    public Set<Country> getTerritoriesIn(Country.Continent continent, Map map) {
        Set<Country> countries = new HashSet<Country>();
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
    public void removeTerritory(Country country) {
        territories.remove(country);
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
