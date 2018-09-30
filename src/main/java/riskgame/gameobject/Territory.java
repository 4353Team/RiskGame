package riskgame.gameobject;

import java.util.ArrayList;
import java.util.List;

public class Territory {
    String name;
    List<Territory> neighbors = new ArrayList<Territory>();
    Player controlledBy;
    int armies;

    public Territory(Player p1) {
        controlledBy = p1;
        armies = 0;
    }

    public void addArmies(int numArmies) {
        armies = armies + numArmies;
    }

    public void removeArmies(int numArmies) {
        armies = armies - numArmies;
    }

    public void attack(Territory territory) throws SelfAttackException {
        if (territory.getControlledBy() == this.controlledBy) {
            throw new SelfAttackException();
        }
    } // return type to be determined

    private Player getControlledBy() {
        return this.controlledBy;
    }

    public int getArmies() {
        return armies;
    }

    public class SelfAttackException extends RiskException {

    }
}