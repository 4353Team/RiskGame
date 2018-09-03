package com.company;

import java.util.ArrayList;
import java.util.List;

public class Map {

    public List<Country> countries;
    public Map() {

        countries = new ArrayList<Country>();
        int troops = 0;
        Player owner = null;

        // North America
        Country c1 = new Country("Alaska",0, owner);
        Country c2 = new Country("Northwest Territory",0, owner);
        Country c3 = new Country("Greenland",0, owner);
        Country c4 = new Country("Alberta",0, owner);
        Country c5 = new Country("Ontario",0, owner);
        Country c6 = new Country("Quebec",0, owner);
        Country c7 = new Country("Western United States",0, owner);
        Country c8 = new Country("Eastern United States",0, owner);
        Country c9 = new Country("Central America",0, owner);

        // South America
        Country c10 = new Country("Venezuela",0, owner);
        Country c11 = new Country("Peru",0, owner);
        Country c12 = new Country("Brazil",0, owner);
        Country c13 = new Country("Argentina",0, owner);

        // Africa
        Country c14 = new Country("North Africa",0, owner);
        Country c15 = new Country("Egypt",0, owner);
        Country c16 = new Country("East Africa",0, owner);
        Country c17 = new Country("Congo",0, owner);
        Country c18 = new Country("South Africa",0, owner);
        Country c19 = new Country("Madagascar",0, owner);

        // Europe
        Country c20 = new Country("Iceland",0, owner);
        Country c21 = new Country("Great Britain",0, owner);
        Country c22 = new Country("Scandinavia",0, owner);
        Country c23 = new Country("Ukraine",0, owner);
        Country c24 = new Country("Northern Europe",0, owner);
        Country c25 = new Country("Western Europe",0, owner);
        Country c26 = new Country("Southern Europe",0, owner);

        // Asia
        Country c27 = new Country("Middle East",0, owner);
        Country c28 = new Country("Afganistan",0, owner);
        Country c29 = new Country("India",0, owner);
        Country c30 = new Country("China",0, owner);
        Country c31 = new Country("Mongolia",0, owner);
        Country c32 = new Country("Southeast Asia",0, owner);
        Country c33 = new Country("Ural",0, owner);
        Country c34 = new Country("Siberia",0, owner);
        Country c35 = new Country("Irkutsk",0, owner);
        Country c36 = new Country("Japan",0, owner);
        Country c37 = new Country("Kamchatka",0, owner);
        Country c38 = new Country("Yakutsk",0, owner);

        // Australia
        Country c39 = new Country("Indonesia",0, owner);
        Country c40 = new Country("New Guinea",0, owner);
        Country c41 = new Country("Western Australia",0, owner);
        Country c42 = new Country("Eastern Australia",0, owner);

        countries.add(c1);
        countries.add(c2);
        countries.add(c3);
        countries.add(c4);
        countries.add(c5);
        countries.add(c6);
        countries.add(c7);
        countries.add(c8);
        countries.add(c9);
        countries.add(c10);
        countries.add(c11);
        countries.add(c12);
        countries.add(c13);
        countries.add(c14);
        countries.add(c15);
        countries.add(c16);
        countries.add(c17);
        countries.add(c18);
        countries.add(c19);
        countries.add(c20);
        countries.add(c21);
        countries.add(c22);
        countries.add(c23);
        countries.add(c24);
        countries.add(c25);
        countries.add(c26);
        countries.add(c27);
        countries.add(c28);
        countries.add(c29);
        countries.add(c30);
        countries.add(c31);
        countries.add(c32);
        countries.add(c33);
        countries.add(c34);
        countries.add(c35);
        countries.add(c36);
        countries.add(c37);
        countries.add(c38);
        countries.add(c39);
        countries.add(c40);
        countries.add(c41);
        countries.add(c42);

        List<Country> c1n = new ArrayList<Country>();
        c1n.add(c2);
        c1n.add(c4);
        c1n.add(c37);
        c1.setNeighbors(c1n);

        List<Country> c2n = new ArrayList<Country>();
        c2n.add(c1);
        c2n.add(c4);
        c2n.add(c5);
        c2n.add(c3);
        c2.setNeighbors(c2n);

        List<Country> c3n = new ArrayList<Country>();
        c3n.add(c2);
        c3n.add(c5);
        c3n.add(c6);
        c3n.add(c20);
        c3.setNeighbors(c3n);

        List<Country> c4n = new ArrayList<Country>();
        c4n.add(c1);
        c4n.add(c2);
        c4n.add(c5);
        c4n.add(c7);
        c4.setNeighbors(c4n);

        List<Country> c5n = new ArrayList<Country>();
        c5n.add(c2);
        c5n.add(c3);
        c5n.add(c4);
        c5n.add(c6);
        c5n.add(c7);
        c5n.add(c8);
        c5.setNeighbors(c5n);

        List<Country> c6n = new ArrayList<Country>();
        c6n.add(c3);
        c6n.add(c5);
        c6n.add(c8);
        c6.setNeighbors(c6n);

        List<Country> c7n = new ArrayList<Country>();
        c7n.add(c4);
        c7n.add(c5);
        c7n.add(c8);
        c7n.add(c9);
        c7.setNeighbors(c7n);

        List<Country> c8n = new ArrayList<Country>();
        c8n.add(c5);
        c8n.add(c7);
        c8n.add(c6);
        c8n.add(c9);
        c8.setNeighbors(c8n);

        List<Country> c9n = new ArrayList<Country>();
        c9n.add(c7);
        c9n.add(c8);
        c9n.add(c10);
        c9.setNeighbors(c9n);
    }
}
