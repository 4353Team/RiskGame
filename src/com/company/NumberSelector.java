package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class NumberSelector extends JComponent implements MouseListener {
    private Location coordinate;

    public Location getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Location coordinate) {
        this.coordinate = coordinate;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getSelectedNumber() {
        return selectedNumber;
    }

    public void setSelectedNumber(int selectedNumber) {
        this.selectedNumber = selectedNumber;
    }

    private Dimension dimension;
    private int lowerBound;
    private int upperBound;
    private boolean visible;

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private int selectedNumber = 0;
    NumberSelector(){
        super();
        lowerBound = 1;
        upperBound = 5;
        visible = true;
        enableInputMethods(true);
        addMouseListener(this);
    }

    @Override
    public void paintComponents(Graphics g) {
        if (visible == true) {
            super.paintComponents(g);
            for (int i = lowerBound; i <= upperBound; i++) {
                g.setColor(Color.white);
                g.setFont(new Font("TimeRoman", Font.PLAIN, 15));
                g.fillRect(coordinate.getX() + (i - 1) * dimension.getWidth(), coordinate.getY(), dimension.getWidth(), dimension.getHeight());
                g.setColor(Color.black);
                g.drawRect(coordinate.getX() + (i - 1) * dimension.getWidth(), coordinate.getY(), dimension.getWidth(), dimension.getHeight());
                g.drawString(i + "", coordinate.getX() + (i - 1) * dimension.getWidth() + dimension.getWidth() / 2 - 3, coordinate.getY() + dimension.getHeight() / 2 + 6);
                g.setFont(new Font("TimeRoman", Font.PLAIN, 10));

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    @Override
    public java.awt.Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    @Override
    public java.awt.Dimension getMaximumSize() {
        return super.getMaximumSize();
    }
}
