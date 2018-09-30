package riskgame.commands;

import riskgame.gameobject.Territory;

/**
 * Commands that need to be made:
 * Attack(Territory from, Territory to)
 * MoveTroops(Country from, Country to)
 * TurnInRiskCard(Player)
 * TakeContinent - could be a subcommand
 * LoseTroops - could be a subcommand
 *
 *
 * When you get full control of a continent you get a certain number of troops
 *
 * Implemented:
 * DraftOneArmy(
 */
public interface Command {
    // can also be used as a 'redo'
    public void execute() throws IllegalExecutionException;

    //reverses every action take by the execute method
    public void undo() throws IllegalUndoException;

    class IllegalUndoException extends Exception {
        public IllegalUndoException(Throwable e) {
            super(e);
        }
    }

    class IllegalExecutionException extends Exception {
        public IllegalExecutionException(Throwable e) {
            super(e);
        }
    }
}
