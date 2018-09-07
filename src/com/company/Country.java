package com.company;

import java.util.List;
import java.util.Random;

public class Country {
    private Location coordinate;

    private String name;

    Continent continent;
    private int troops;
    private Player owner;
    private List<Country> neighbors;


    enum Continent {
        NORTH_AMERICA, SOUTH_AMERICA, AFRICA, EUROPE, ASIA, AUSTRALIA
    }
    public Country(String name, Continent continent, int troops, Player owner) {
        this.name = name;
        this.troops = troops;
        this.owner = owner;
        this.neighbors = null;
        this.continent = continent;
        this.coordinate = new Location();

    }

    public Location getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Location coordinate) {
        this.coordinate = coordinate;
    }

    public Country(String name){
        this.name = name;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public Player getOwner() {
        return owner;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTroops(int troops) {
        this.troops = troops;
    }
    public int getTroops() {
        return troops;
    }
    public List<Country> getNeighbors() {
        return neighbors;
    }
    public void setNeighbors(List<Country> neighbors) {
        this.neighbors = neighbors;
    }
    public void addInfantry(Player p) {
        troops += 1;
        owner = p;
        p.setTotalInitialTroops(p.getTotalInitialTroops()-1);
    }
    public void removeTroops(int number) {
        this.troops = this.troops - number;
        System.out.println("Removing " + number + " troop from " + getName());
    }

}
