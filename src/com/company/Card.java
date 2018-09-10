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

    @Override
    public String toString() {
        return territory.getName() + " | " + troopsType.toString();
    }
}
