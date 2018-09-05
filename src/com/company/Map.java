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
        Country c1 = new Country("Alaska", Country.Continent.NORTH_AMERICA,0, owner);
        Country c2 = new Country("Northwest Territory", Country.Continent.NORTH_AMERICA, 0, owner);
        Country c3 = new Country("Greenland",Country.Continent.NORTH_AMERICA, 0, owner);
        Country c4 = new Country("Alberta",Country.Continent.NORTH_AMERICA, 0, owner);
        Country c5 = new Country("Ontario",Country.Continent.NORTH_AMERICA, 0, owner);
        Country c6 = new Country("Quebec",Country.Continent.NORTH_AMERICA, 0, owner);
        Country c7 = new Country("Western United States",Country.Continent.NORTH_AMERICA, 0, owner);
        Country c8 = new Country("Eastern United States",Country.Continent.NORTH_AMERICA, 0, owner);
        Country c9 = new Country("Central America",Country.Continent.NORTH_AMERICA, 0, owner);

        // South America
        Country c10 = new Country("Venezuela",Country.Continent.SOUTH_AMERICA,0, owner);
        Country c11 = new Country("Peru",Country.Continent.SOUTH_AMERICA,0, owner);
        Country c12 = new Country("Brazil",Country.Continent.SOUTH_AMERICA,0, owner);
        Country c13 = new Country("Argentina",Country.Continent.SOUTH_AMERICA,0, owner);
        // Africa
        Country c14 = new Country("North Africa",Country.Continent.AFRICA,0, owner);
        Country c15 = new Country("Egypt",Country.Continent.AFRICA,0, owner);
        Country c16 = new Country("East Africa",Country.Continent.AFRICA,0, owner);
        Country c17 = new Country("Congo",Country.Continent.AFRICA,0, owner);
        Country c18 = new Country("South Africa",Country.Continent.AFRICA,0, owner);
        Country c19 = new Country("Madagascar",Country.Continent.AFRICA,0, owner);

        // Europe
        Country c20 = new Country("Iceland",Country.Continent.EUROPE,0, owner);
        Country c21 = new Country("Great Britain",Country.Continent.EUROPE,0, owner);
        Country c22 = new Country("Scandinavia",Country.Continent.EUROPE,0, owner);
        Country c23 = new Country("Ukraine",Country.Continent.EUROPE,0, owner);
        Country c24 = new Country("Northern Europe",Country.Continent.EUROPE,0, owner);
        Country c25 = new Country("Western Europe",Country.Continent.EUROPE,0, owner);
        Country c26 = new Country("Southern Europe",Country.Continent.EUROPE,0, owner);

        // Asia
        Country c27 = new Country("Middle East",Country.Continent.ASIA,0, owner);
        Country c28 = new Country("Afganistan",Country.Continent.ASIA,0, owner);
        Country c29 = new Country("India",Country.Continent.ASIA,0, owner);
        Country c30 = new Country("China",Country.Continent.ASIA,0, owner);
        Country c31 = new Country("Mongolia",Country.Continent.ASIA,0, owner);
        Country c32 = new Country("Southeast Asia",Country.Continent.ASIA,0, owner);
        Country c33 = new Country("Ural",Country.Continent.ASIA,0, owner);
        Country c34 = new Country("Siberia",Country.Continent.ASIA,0, owner);
        Country c35 = new Country("Irkutsk",Country.Continent.ASIA,0, owner);
        Country c36 = new Country("Japan",Country.Continent.ASIA,0, owner);
        Country c37 = new Country("Kamchatka",Country.Continent.ASIA,0, owner);
        Country c38 = new Country("Yakutsk",Country.Continent.ASIA,0, owner);

        // Australia
        Country c39 = new Country("Indonesia",Country.Continent.AUSTRALIA,0, owner);
        Country c40 = new Country("New Guinea",Country.Continent.AUSTRALIA,0, owner);
        Country c41 = new Country("Western Australia",Country.Continent.AUSTRALIA,0, owner);
        Country c42 = new Country("Eastern Australia",Country.Continent.AUSTRALIA,0, owner);

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

        // north america
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

        //south america
        List<Country> c10n = new ArrayList<Country>();
        c10n.add(c9);
        c10n.add(c11);
        c10n.add(c12);
        c10.setNeighbors(c10n);

        List<Country> c11n = new ArrayList<Country>();
        c11n.add(c10);
        c11n.add(c12);
        c11n.add(c13);
        c11.setNeighbors(c11n);

        List<Country> c12n = new ArrayList<Country>();
        c12n.add(c10);
        c12n.add(c11);
        c12n.add(c13);
        c12n.add(c14);
        c12.setNeighbors(c12n);

        List<Country> c13n = new ArrayList<Country>();
        c13n.add(c11);
        c13n.add(c12);
        c13.setNeighbors(c13n);

        List<Country> c14n = new ArrayList<Country>();
        c14n.add(c12);
        c14n.add(c25);
        c14n.add(c15);
        c14n.add(c16);
        c14n.add(c17);

        // africa
        c14.setNeighbors(c14n);

        List<Country> c15n = new ArrayList<Country>();
        c15n.add(c14);
        c15n.add(c26);
        c15n.add(c27);
        c15n.add(c16);
        c15.setNeighbors(c15n);

        List<Country> c16n = new ArrayList<Country>();
        c16n.add(c15);
        c16n.add(c14);
        c16n.add(c17);
        c16n.add(c18);
        c16n.add(c19);
        c16n.add(c27);
        c16.setNeighbors(c16n);

        List<Country> c17n = new ArrayList<Country>();
        c17n.add(c14);
        c17n.add(c16);
        c17n.add(c18);
        c17.setNeighbors(c17n);

        List<Country> c18n = new ArrayList<Country>();
        c18n.add(c17);
        c18n.add(c16);
        c18n.add(c19);
        c18.setNeighbors(c18n);

        List<Country> c19n = new ArrayList<Country>();
        c19n.add(c18);
        c19n.add(c16);
        c19.setNeighbors(c19n);

        // europe

        List<Country> c20n = new ArrayList<Country>();
        c20n.add(c3);
        c20n.add(c21);
        c20n.add(c22);
        c20.setNeighbors(c20n);

        List<Country> c21n = new ArrayList<Country>();
        c21n.add(c20);
        c21n.add(c24);
        c21n.add(c22);
        c21n.add(c25);

        c21.setNeighbors(c21n);

        List<Country> c22n = new ArrayList<Country>();
        c22n.add(c20);
        c22n.add(c21);
        c22n.add(c24);
        c22n.add(c23);
        c22.setNeighbors(c22n);

        List<Country> c23n = new ArrayList<Country>();
        c23n.add(c22);
        c23n.add(c24);
        c23n.add(c26);
        c23n.add(c27);
        c23n.add(c28);
        c23n.add(c33);
        c23.setNeighbors(c23n);

        List<Country> c24n = new ArrayList<Country>();
        c24n.add(c21);
        c24n.add(c22);
        c24n.add(c25);
        c24n.add(c26);
        c24n.add(c23);
        c24.setNeighbors(c24n);

        List<Country> c25n = new ArrayList<Country>();
        c25n.add(c24);
        c25n.add(c26);
        c25n.add(c14);
        c25n.add(c21);
        c25.setNeighbors(c25n);

        List<Country> c26n = new ArrayList<Country>();
        c26n.add(c24);
        c26n.add(c25);
        c26n.add(c15);
        c26n.add(c27);
        c26n.add(c23);
        c26.setNeighbors(c26n);

        List<Country> c27n = new ArrayList<Country>();
        c27n.add(c23);
        c27n.add(c26);
        c27n.add(c15);
        c27n.add(c16);
        c27n.add(c28);
        c27n.add(c29);
        c27.setNeighbors(c27n);

        List<Country> c28n = new ArrayList<Country>();
        c28n.add(c23);
        c28n.add(c27);
        c28n.add(c29);
        c28n.add(c30);
        c28n.add(c33);
        c28.setNeighbors(c28n);

        List<Country> c29n = new ArrayList<Country>();
        c29n.add(c27);
        c29n.add(c28);
        c29n.add(c30);
        c29n.add(c32);
        c29.setNeighbors(c29n);

        List<Country> c30n = new ArrayList<Country>();
        c30n.add(c32);
        c30n.add(c29);
        c30n.add(c28);
        c30n.add(c33);
        c30n.add(c34);
        c30n.add(c31);
        c30.setNeighbors(c30n);

        List<Country> c31n = new ArrayList<Country>();
        c31n.add(c30);
        c31n.add(c34);
        c31n.add(c35);
        c31n.add(c37);
        c31n.add(c36);
        c31.setNeighbors(c31n);

        List<Country> c32n = new ArrayList<Country>();
        c32n.add(c29);
        c32n.add(c30);
        c32n.add(c39);
        c32.setNeighbors(c32n);

        List<Country> c33n = new ArrayList<Country>();
        c33n.add(c23);
        c33n.add(c34);
        c33n.add(c30);
        c33n.add(c28);
        c33.setNeighbors(c33n);

        List<Country> c34n = new ArrayList<Country>();
        c34n.add(c33);
        c34n.add(c30);
        c34n.add(c31);
        c34n.add(c35);
        c34n.add(c38);
        c34.setNeighbors(c34n);

        List<Country> c35n = new ArrayList<Country>();
        c35n.add(c31);
        c35n.add(c34);
        c35n.add(c38);
        c35n.add(c37);
        c35.setNeighbors(c35n);

        List<Country> c36n = new ArrayList<Country>();
        c36n.add(c37);
        c36n.add(c31);
        c36.setNeighbors(c36n);

        List<Country> c37n = new ArrayList<Country>();
        c37n.add(c38);
        c37n.add(c35);
        c37n.add(c36);
        c37n.add(c1);
        c37n.add(c31);

        c37.setNeighbors(c37n);

        List<Country> c38n = new ArrayList<Country>();
        c38n.add(c34);
        c38n.add(c35);
        c38n.add(c37);
        c38.setNeighbors(c38n);

        // australia
        List<Country> c39n = new ArrayList<Country>();
        c39n.add(c32);
        c39n.add(c40);
        c39n.add(c41);
        c39.setNeighbors(c39n);

        List<Country> c40n = new ArrayList<Country>();
        c40n.add(c39);
        c40n.add(c41);
        c40n.add(c42);
        c40.setNeighbors(c40n);

        List<Country> c41n = new ArrayList<Country>();
        c41n.add(c39);
        c41n.add(c40);
        c41n.add(c42);
        c41.setNeighbors(c41n);

        List<Country> c42n = new ArrayList<Country>();
        c42n.add(c40);
        c42n.add(c41);
        c42.setNeighbors(c42n);
    }

    @Override
    public String toString() {
        String output = "";
        try {
            for (Country d : countries) {
                output = output + ((d.getOwner() != null)?d.getOwner().getName():"No one ")+ " owns " + d.getName() + " with " + d.getTroops() + " troops and borders ";
                for (Country c : d.getNeighbors()) {
                   output = output + c.getName() + " , ";
                }
                output = output + "\n";
            }



        } catch (Exception e) {
        }
        return output;
    }
    public int getTotalTroops() {
        int t = 0;
        for(Country c : countries) {
            t = t + c.getTroops();
        }
        return t;
    }

}
