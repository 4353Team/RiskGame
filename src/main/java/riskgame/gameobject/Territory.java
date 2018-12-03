package riskgame.gameobject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import riskgame.GameEngine;
import riskgame.SingleUIGame;
import riskgame.commands.Command;
import riskgame.gameobject.player.Observable;
import riskgame.gameobject.player.Observer;
import riskgame.gameobject.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Territory implements Serializable, Observable {
    public static final Player NoOwner = new Player("NO OWNER");
    private static final Logger logger = LogManager.getLogger(Territory.class);
    String name;
    List<Territory> neighbors = new ArrayList<Territory>();
    Player controlledBy = NoOwner; // default unowned territory
    ArrayList<Observer> observers;
    int armies;

    public Territory(String name) {
        this.name = name;
        NoOwner.addTerritory(this);
        armies = 0;
        observers = new ArrayList<Observer>();
    }

    public Territory(Player p1) {
        observers = new ArrayList<Observer>();
        controlledBy = p1;
        armies = 0;
    }

    public Territory(Player p1, String name) {
        observers = new ArrayList<Observer>();
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

    @Override
    public void register(Observer inObserver) {
        observers.add(inObserver);
    }

    @Override
    public void unregister(Observer inObserver) {
        observers.remove(inObserver);
    }

    @Override
    public void notifyObserver(Observer inObserver) {

    }

    public static class Attack implements Command {
        private Territory attackingTerritory;
        private Territory defendingTerritory;

        private final SingleUIGame game;

        private Player defendingPlayerBefore;

        int attackingArmyBefore;
        int defendingArmyBefore;

        private int attackingArmyAfter;
        private int defendingArmyAfter;

        private final int attackDices;
        private final int defenseDices;

        private final List<Integer> attackingNums;
        private final List<Integer> defendingNums;

        Territory gainedTerritoryByAttacker = null;

        private boolean attackWon = false;

        public Attack(Territory attacking, Territory defending, SingleUIGame game) {
            this.attackingTerritory = attacking;
            this.defendingTerritory = defending;
            this.game = game;
            // Randomization MUST be done in the constructor to prevent undo/redo from having different effect

            this.attackDices = (attackingTerritory.getArmies() - 1) > 3 ? 3 : (attackingTerritory.getArmies() - 1);
            this.defenseDices = (defendingTerritory.getArmies() > 2) ? 2 : 1;

            Dice dice = new Dice();

            attackingNums = dice.roll(attackDices).diceFaces;
            defendingNums = dice.roll(defenseDices).diceFaces;
        }

        /**
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
                        attackWon = true;
                        game.attackWon();
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
            assert (gainedTerritoryByAttacker == null) != attackWon; // attackWon should not be true when territoryGainedByAttacker is null and vice versa
            if (attackWon) { // also gainedTerritoryByAttacker != null
                attackingTerritory.getControlledBy().removeTerritory(defendingTerritory);
                defendingPlayerBefore.addTerritory(defendingTerritory);
                defendingTerritory.setControlledBy(defendingPlayerBefore);
                game.attackWonUndo();
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

    public static class MoveArmies implements Command {
        private final int armiesToMoveIn;
        private final Territory fromTerritory;
        private final Territory toTerritory;

        public MoveArmies(int armiesToMoveIn, Territory fromTerritory, Territory toTerritory) {
            this.armiesToMoveIn = armiesToMoveIn;
            this.fromTerritory = fromTerritory;
            this.toTerritory = toTerritory;
        }

        @Override
        public void log() {
            logger.info("Moving territories....");
        }

        @Override
        public void execute() throws IllegalExecutionException {
            fromTerritory.armies -= armiesToMoveIn;
            toTerritory.armies += armiesToMoveIn;
            assert (fromTerritory.armies > 0);
            assert (toTerritory.armies > 0); // ? is this always true
        }

        @Override
        public void undo() throws IllegalUndoException {
            fromTerritory.armies += armiesToMoveIn;
            toTerritory.armies -= armiesToMoveIn;
            assert (fromTerritory.armies > 0);
            assert (toTerritory.armies > 0); // ? is this always true
        }
    }

    private class NegativeArmiesException extends Throwable {
    }

    public static class AttackPick {
        public final Territory attackingTerritory;
        public final Territory defendingTerritory;
        public AttackPick(Territory attackingTerritory, Territory defendingTerritory) {
            this.attackingTerritory = attackingTerritory;
            this.defendingTerritory = defendingTerritory;
        }

        public void checksOut(Player player) throws AttackPickException {
            if (attackingTerritory.getControlledBy() != player) throw new AttackPickException("Please choose a " +
                    "territory you own to attack from");
            if (defendingTerritory.getControlledBy() == player) throw new AttackPickException("You may not attack " +
                    "yourself");
        }

        public class AttackPickException extends Exception {
            AttackPickException(String message){
                super(message);
            }
        }
    }
}