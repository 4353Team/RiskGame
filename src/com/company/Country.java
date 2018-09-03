package com.company;

import java.util.List;

public class Country {
    private String name;
    private int troops;
    private Player owner;
    private List<Country> neighbors;
    public Country(String name, int troops, Player owner) {
        this.name = name;
        this.troops = troops;
        this.owner = owner;
    }

    public int getTroops() {
        return troops;
    }
    public Player getOwner() {
        return owner;
    }
    public String getName(){
        return name;
    }

    public void setNeighbors(List<Country> neighbors) {
        this.neighbors = neighbors;
    }
}
