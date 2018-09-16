package riskgame;

import java.util.List;
import java.util.Random;

public class Continent {
    private String name;                // fields
    private List<Territory> countries;
    private int id;
    private int bonusTroops;
    Continent continentlist;



    public int getbonusTroops() {       // get method

        return bonusTroops;

    }
    public Continent(String name, int bonusTroops, List<Territory> memberCountries){
        this.name = name;               // constructor
        this.bonusTroops = bonusTroops;
        countries = memberCountries;

    }

    public String getName() {
        return name;
    }

  /*  public int setbonusTroops(){          // need?

    }*/


    public List<Territory> getCountries(){

        return countries;
    }

    public int getId(){
        return id;

    }

    /*public boolean isPartof(Country c){
        return (countries.contains(c.get))

    }*/

    // maybe make a bonusTroops class

    // event class, to represent attacks



}
