package com.company;

import org.junit.Test;

public class CountryTest {
    private Dimension d1 = new Dimension(50,15);
    private Location l1 = new Location(0,0);
    private Player Evelyn = new Player("Evelyn");

    @Test
    public void addTroops(){
        //size of the Territory
        Country Dolce = new Country("Dolce", Country.Continent.SOUTH_AMERICA,0,Evelyn, d1,l1);
        Dolce.addTroops(5);
        int expected = 5;
        int actual = Dolce.getTroops();
        assert actual == expected: actual + "troops were added to the country instead of the expected" + expected +  " + troops";
    }
}
