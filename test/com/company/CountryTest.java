package com.company;

import org.junit.Test;

import java.sql.SQLOutput;

public class CountryTest {
    private Dimension d1 = new Dimension(50,15);
    private Location l1 = new Location(0,0);
    private Location l2 = new Location (23,60);
    private Player Evelyn = new Player("Evelyn");
    private Player Morton = new Player("Morton");
    Country Dolce = new Country("Dolce", Country.Continent.SOUTH_AMERICA,67,Evelyn, d1,l1);
    Country Lager = new Country("Lager", Country.Continent.EUROPE,4,Morton, d1,l2);


    // Test: Fortify Phase
    //test that the troops were moved from one country to the other
    public void moveTroops(){

        //int actual = Dolce.getTroops();
        //assert actual == expected: actual + "troops were added to the country instead of the expected" + expected +  " + troops";
    }

    @Test
    public void noTerritoryAbandonded(){
        boolean territoryAbandoned = false;
        int troopsInDolce = Dolce.getTroops();
        try {
            Dolce.removeTroops(troopsInDolce);
            territoryAbandoned = true;
            System.out.println("Country has been abandoned!");
        }catch( Exception e){
            System.out.println("Must keep at least one troop in Country");
        }
        assert !territoryAbandoned;
    }
    public void oneFortifyPerTurn(){}

    public void neighboringCountries(){

    }
}
