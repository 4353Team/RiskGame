package riskgame.gameobject;

import riskgame.commands.Command;
import riskgame.gameobject.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Territory {
    public static final Player NoOwner = new Player("NO OWNER");
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

    public int getArmies() {
        return armies;
    }

    private void changeArmies(int changeInArmies) throws NegativeArmiesException {
        armies += changeInArmies;
        if (armies < 0) throw new NegativeArmiesException();
    }

    public void addNeighbor(Territory t2) {
        neighbors.add(t2);
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
            for (int i = 0; i < lowest;i++) {
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
    }




    public String getName() {
        return name;
    }

    public void setControlledBy(Player controlledBy) {
        this.controlledBy = controlledBy;
    }

    private void setArmies(int armies) {
        this.armies = armies;
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

    public static class SelfAttackException extends RiskException {

    }

    private class NegativeArmiesException extends Throwable {
    }

}