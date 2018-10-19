package riskgame.gameobject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import riskgame.commands.Command;
import riskgame.gameobject.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Territory implements Serializable {
    public static final Player NoOwner = new Player("NO OWNER");
    private static final Logger logger = LogManager.getLogger(Territory.class);
    String name;
    List<Territory> neighbors = new ArrayList<Territory>();
    Player controlledBy = NoOwner; // default unowned territory
    int armies;

    public Territory(String name) {
        this.name = name;
        NoOwner.addTerritory(this);
        armies = 0;
    }

    public Territory(Player p1) {
        controlledBy = p1;
        armies = 0;
    }

    public Territory(Player p1, String name) {
        controlledBy = p1;
        armies = 0;
        this.name = name;
    }

    public void addArmies(int numArmies) {
        armies = armies + numArmies;
    }

    public void removeArmies(int numArmies) {
        armies = armies - numArmies;
    }

    public Player getControlledBy() {
        return this.controlledBy;
    }

    public void setControlledBy(Player controlledBy) {
        this.controlledBy = controlledBy;
    }

    public int getArmies() {
        return armies;
    }

    private void setArmies(int armies) {
        this.armies = armies;
    }

    private void changeArmies(int changeInArmies) throws NegativeArmiesException {
        armies += changeInArmies;
        if (armies < 0) throw new NegativeArmiesException();
    }

    public void addNeighbor(Territory t2) {
        neighbors.add(t2);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static class Attack implements Command {
        private Territory attackingTerritory;
        private Territory defendingTerritory;

        private Player defendingPlayerBefore;

        int attackingArmyBefore;
        int defendingArmyBefore;

        private int attackingArmyAfter;
        private int defendingArmyAfter;

        Territory gainedTerritoryByAttacker = null;

        public Attack(Territory attacking, Territory defending) {
            this.attackingTerritory = attacking;
            this.defendingTerritory = defending;
        }

        /**
         * ToDo: move all calculation and randomization code to constructor and keep mutable code
         * ToDo: split this command into two commands, the second of which, the user chooses how many troops to move to new
         * ToDo: Territory if conquer is successful (only after conquering the territory)
         *
         * @throws IllegalExecutionException
         */
        @Override
        public void execute() throws IllegalExecutionException {
            if (attackingTerritory.getControlledBy() == defendingTerritory.getControlledBy())
                throw new IllegalExecutionException(new SelfAttackException());
            attackingArmyBefore = attackingTerritory.getArmies();
            defendingArmyBefore = defendingTerritory.getArmies();
            defendingPlayerBefore = defendingTerritory.getControlledBy();

            int attackDices = (attackingTerritory.getArmies() - 1) > 3 ? 3 : (attackingTerritory.getArmies() - 1);
            int defenseDices = (defendingTerritory.getArmies() > 2) ? 2 : 1;

            Dice dice = new Dice();

            List<Integer> attackingNums = dice.roll(attackDices).diceFaces;
            List<Integer> defendingNums = dice.roll(defenseDices).diceFaces;

            int lowest = (attackDices > defenseDices) ? defenseDices : attackDices;
            int numberAttackersRemoved = 0;
            for (int i = 0; i < lowest; i++) {
                if (attackingNums.get(i) > defendingNums.get(i)) {
                    // attack winner
                    System.out.println(attackingTerritory.getName() + " wins");
                    defendingTerritory.removeArmies(1);
                    if (defendingTerritory.getArmies() == 0) { // if invaded
                        defendingTerritory.setControlledBy(attackingTerritory.getControlledBy());
                        gainedTerritoryByAttacker = defendingTerritory;
                        // occupy territory
                        System.out.println(attackingTerritory.getName() + " captures " + defendingTerritory.getName() + "!!");
                        //territoriesCapturedThisTurn += 1;

                        // todo: make a separate command
                        int movetroops = 2;

                        System.out.printf("%s decides to move %d troop%s from %s to %s\n", attackingTerritory.getName(), movetroops, (movetroops == 1) ? "" : "s", attackingTerritory.getName(), defendingTerritory.getName());
                        if (attackingTerritory.getArmies() - movetroops >= 1) {
                            defendingTerritory.setArmies(movetroops);
                            defendingTerritory.setControlledBy(attackingTerritory.getControlledBy());
                            attackingTerritory.getControlledBy().addTerritory(defendingTerritory);
                            defendingTerritory.getControlledBy().removeTerritory(defendingTerritory);
                            attackingTerritory.removeArmies(movetroops);
                        } else
                            System.out.println("Cannot move " + movetroops + ". You need to leave at least one troop behind.");
                    }
                } else {
                    // defense winner
                    System.out.println("defender" + " wins");
                    if (numberAttackersRemoved < 2)
                        attackingTerritory.removeArmies(1);
                    numberAttackersRemoved += 1;
                }
                attackingArmyAfter = attackingTerritory.getArmies();
                defendingArmyAfter = defendingTerritory.getArmies();
            }
        }

        @Override
        public void undo() throws IllegalUndoException {
            attackingTerritory.setArmies(attackingArmyBefore);
            defendingTerritory.setArmies(defendingArmyBefore);
            if (gainedTerritoryByAttacker != null) {
                attackingTerritory.getControlledBy().removeTerritory(defendingTerritory);
                defendingPlayerBefore.addTerritory(defendingTerritory);
                defendingTerritory.setControlledBy(defendingPlayerBefore);
            }
        }

        @Override
        public void log() {
            logger.warn("We haven't implemented attack yet!!!");
        }
    }

    public static class DraftOneArmy implements Command, Serializable {
        Territory territory;

        public DraftOneArmy(Territory t) {
            territory = t;
        }

        /**
         * Logs the action of the command
         */
        @Override
        public void log() {
            logger.info("One army drafted to Territory: " + territory.getName() + ", controlled by Player: " +
                    territory.getControlledBy().getName());
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

    public static class SelfAttackException extends RiskException {

    }

    private class NegativeArmiesException extends Throwable {
    }

}