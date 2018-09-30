package riskgame.gameobject;

import riskgame.commands.Command;
import riskgame.gameobject.player.Player;

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

    private Player getControlledBy() {
        return this.controlledBy;
    }

    public int getArmies() {
        return armies;
    }

    private void changeArmies(int changeInArmies) throws NegativeArmiesException {
        armies += changeInArmies;
        if (armies < 0) throw new NegativeArmiesException();
    }

    public static class Attack implements Command {
        private Territory attackingTerritory;
        private Territory defendingTerritory;
        private int changeInArmies_attacking = 0;
        private int changeInArmies_defending = 0;

        public Attack(Territory attacking, Territory defending) {
            this.attackingTerritory = attacking;
            this.defendingTerritory = defending;
        }

        @Override
        public void execute() {
            int attackDices = (attackingTerritory.getArmies() - 1) > 3 ? 3 : (attackingTerritory.getArmies() - 1);
            int defenseDices = (defendingTerritory.getArmies() > 2) ? 2 : 1;
        }

        @Override
        public void undo() throws IllegalUndoException {
            try {
                attackingTerritory.changeArmies(changeInArmies_attacking);
                defendingTerritory.changeArmies(changeInArmies_defending);
            } catch (NegativeArmiesException e) {
                throw new IllegalUndoException(e);
            }

        }
    }

    public static class DraftOneArmy implements Command {
        Territory territory;

        public DraftOneArmy(Territory t) {
            territory = t;
        }

        @Override
        public void execute() {
            territory.addArmies(1);
        }

        @Override
        public void undo() {
            territory.removeArmies(1);
        }
    }

    public class SelfAttackException extends RiskException {

    }

    private class NegativeArmiesException extends Throwable {
    }
}