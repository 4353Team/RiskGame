package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Country extends JFrame {
    private Location coordinate;
    private Dimension dimension;
    private String name;
    private int troops;

    private Player ownedBy;
    private List<Country> neighbors;
    Continent continent;
    enum Continent {
        NORTH_AMERICA, SOUTH_AMERICA, AFRICA, EUROPE, ASIA, AUSTRALIA
    }
    public Country(String name, Continent continent, int troops, Player owner,Dimension d, Location c) {
        this.name = name;
        this.troops = troops;
        this.ownedBy = owner;
        this.neighbors = null;
        this.continent = continent;
        this.coordinate = c;
        this.dimension = d;



    }
    public Location getCoordinate() {
        return coordinate;
    }
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
    public Dimension getDimension() {
        return dimension;
    }
    public void setCoordinate(Location coordinate) {
        this.coordinate = coordinate;
    }
    public Country(String name){
        this.name = name;
    }
    public void setOwnedBy(Player owner) {
        this.ownedBy = owner;
    }
    public Player getOwnedBy() {
        return ownedBy;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTroops(int troops) {
        this.troops = troops;
    }
    public int getTroops() {
        return troops;
    }
    public List<Country> getNeighbors() {
        return neighbors;
    }
    public void setNeighbors(List<Country> neighbors) {
        this.neighbors = neighbors;
    }
    public void addOneInfantry(Player p) {
        troops += 1;
        if (ownedBy != p)
            ownedBy = p;
        p.setTotalInitialTroops(p.getTotalInitialTroops()-1);
    }
    public void addTroops(int number) {
        this.troops = this.troops + number;
        System.out.println("Adding " + number + " troop to " + getName());
    }
    public void removeTroops(int number) {
        this.troops = this.troops - number;
        System.out.println("Removing " + number + " troop from " + getName());
    }

    @Override
    public boolean contains(Point p) {
   //     System.out.println("Point is +" + p.getX()+ " " + p.getY());
        if ((p.getX() >= coordinate.getX()) && (p.getX() <= (coordinate.getX()+dimension.getWidth()))) {
            if ((p.getY() >= coordinate.getY()) && (p.getY() <= (coordinate.getY()+dimension.getHeight()))){
                return true;
            }
        }
        return false;
    }


    public void popOut(Graphics g){
     //   super.paintComponents(g);
        g.setColor(Color.red);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(8));
        g.drawRect(this.coordinate.getX(),this.coordinate.getY(),getDimension().getWidth(),getDimension().getHeight());
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.black);
        g.drawRect(this.coordinate.getX()-4,this.coordinate.getY()-4,getDimension().getWidth()+8,getDimension().getHeight()+8);

    }

    public void drawDraftPhase(Graphics g) {

        int circleSize = 30;
        int xxx = getCoordinate().getCenter(getDimension().getWidth(), getDimension().getHeight()).getX();
        int yyy = getCoordinate().getCenter(getDimension().getWidth(), getDimension().getHeight()).getY();
        int xx = coordinate.getCenter(dimension.getWidth(), dimension.getWidth()).getX() - circleSize / 2;
        int yy = coordinate.getCenter(dimension.getHeight(), dimension.getHeight()).getY() - circleSize / 2;
        g.setColor(Color.white);

        g.fillOval(xx, yy, circleSize, circleSize);
        g.setColor(Color.black);
        circleSize = circleSize -1;


        g.setFont(new Font("TimeRoman", Font.PLAIN, 14));
        String yx = getTroops()+"";
        g.drawString(yx, xxx - (yx.length() * 4), yyy + 6);
        g.setFont(new Font("TimeRoman", Font.PLAIN, 10));
    }
    public void drawAttackPhase(Graphics g) {
        int circleSize = 30;
        int x = coordinate.getCenter(dimension.getWidth(), dimension.getWidth()).getX() - circleSize / 2;
        int y = coordinate.getCenter(dimension.getHeight(), dimension.getHeight()).getY() - circleSize / 2;

        Graphics2D g2d = (Graphics2D) g;
        for (Country c : neighbors
        ) {
            if (c.getOwnedBy() == this.getOwnedBy()) {
                g.setColor(Color.darkGray);
            } else {
                g.setColor(Color.white);
            }
            int xx = c.getCoordinate().getCenter(c.getDimension().getWidth(), c.getDimension().getWidth()).getX() - circleSize / 2;
            int yy = c.getCoordinate().getCenter(c.getDimension().getHeight(), c.getDimension().getHeight()).getY() - circleSize / 2;

            g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10));

            g.drawLine(x + circleSize / 2, y + circleSize / 2, xx + circleSize / 2, yy + circleSize / 2);
            g.fillOval(xx, yy, circleSize, circleSize);

            // g2d.setStroke(new BasicStroke(circleSize/10));
            int xxx = c.getCoordinate().getCenter(c.getDimension().getWidth(), c.getDimension().getHeight()).getX();
            int yyy = c.getCoordinate().getCenter(c.getDimension().getWidth(), c.getDimension().getHeight()).getY();
            String num = String.valueOf(c.getTroops());
            if (c.getOwnedBy() == this.getOwnedBy()) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.red);
            }
            g.setFont(new Font("TimeRoman", Font.PLAIN, 14));
            g.drawString(num, xxx - (num.length() * 4), yyy + 6);


            xxx = getCoordinate().getCenter(getDimension().getWidth(), getDimension().getHeight()).getX();
            yyy = getCoordinate().getCenter(getDimension().getWidth(), getDimension().getHeight()).getY();
            g.setColor(Color.white);
            //   g2d.setStroke(new BasicStroke(circleSize-24));
            g.fillOval(x, y, circleSize, circleSize);
            g.setColor(new Color(20, 160, 30));
            g.setFont(new Font("TimeRoman", Font.PLAIN, 14));
            String yx = getTroops()+"";
            g.drawString(yx, xxx - (yx.length() * 4), yyy + 6);
            g.setFont(new Font("TimeRoman", Font.PLAIN, 10));
        }
    }
    public void drawFortifyPhase(Graphics g) {

    }

    public void paintComponents(Graphics g) {
     //   super.paintComponents(g);
//        try {
//
//            Thread.sleep(2000);
//        } catch (Exception e) {}
        if (this.ownedBy == null) {
            g.setColor(Color.gray);
        } else {
            g.setColor(this.ownedBy.getPlayerColor());
        }
        g.fillRect(this.coordinate.getX(),this.coordinate.getY(),getDimension().getWidth(),getDimension().getHeight());
        g.setColor(Color.black);
        g.drawRect(this.coordinate.getX(),this.coordinate.getY(),getDimension().getWidth(),getDimension().getHeight());

        int x = getCoordinate().getCenter(getDimension().getWidth(),getDimension().getHeight()).getX();

        int y= getCoordinate().getCenter(getDimension().getWidth(),getDimension().getHeight()).getY();
        String num = String.valueOf(getTroops());

        g.setFont(new Font("TimeRoman",Font.PLAIN,14));
        g.drawString(num,x-(num.length()*5),y+8);
        g.setFont(new Font("Dialog",Font.PLAIN,10));
        g.drawString(getName().toUpperCase(),coordinate.getX()+5,coordinate.getY()+15);
    }
    public void drawBorder(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10));
        g.setColor(Color.white);
        g.drawRect(this.coordinate.getX(),this.coordinate.getY(),getDimension().getWidth(),getDimension().getHeight());

    }


}
