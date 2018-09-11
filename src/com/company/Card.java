package com.company;

public class Card {
    private Country territory;
    protected TroopsType troopsType;
    public enum TroopsType {
        INFANTRY, CAVALRY, ARTILLERY
    }
    public Card() {

    }
    public Card(Country country, TroopsType troopsType) {
        this.territory = country;
        this.troopsType = troopsType;
    }

    public Country getTerritory() {
        return territory;
    }

    public void setTerritory(Country territory) {
        this.territory = territory;
    }

    @Override
    public String toString() {
        return territory.getName() + " | " + troopsType.toString();
    }
}
