package com.company;

import javax.swing.*;
import java.awt.*;

public class Card extends JFrame {
    private Country territory;
    private Location coordinate;
    private Player ownedBy;

    public Player getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Player ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Location getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Location coordinate) {
        this.coordinate = coordinate;
    }

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

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        if (territory.getOwnedBy() != null)
            g.setColor(territory.getOwnedBy().getPlayerColor());
        else {
            g.setColor(Color.red);
        }




        g.fillRect(coordinate.getX(),coordinate.getY(),120,170);
        g.setColor(Color.white);
        g.setFont(new Font("TimeRoman",Font.PLAIN,14));
        g.drawString(troopsType.name(),coordinate.getX()+10,coordinate.getY()+20);
        g.setFont(new Font("TimeRoman", Font.PLAIN,  10));
        g.drawString(territory.getName().toUpperCase(),coordinate.getX()+10,coordinate.getY()+150);
        if(getOwnedBy() == territory.getOwnedBy()) {
            g.setColor(Color.lightGray);
            g.setFont(new Font("TimeRoman", Font.PLAIN,  40));
            g.drawString("+2",coordinate.getX()+10,coordinate.getY()+120);
            g.setFont(new Font("TimeRoman", Font.PLAIN,  10));
        }
    }
}
