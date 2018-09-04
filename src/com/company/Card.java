package com.company;

public class Card {
    private Country territory;
    private TroopsType troopsType;
    public enum TroopsType {
        INFANTRY, CAVALRY, ARTILLERY
    }
    public Card(Country country, TroopsType troopsType) {
        this.territory = country;
        this.troopsType = troopsType;
    }
}
