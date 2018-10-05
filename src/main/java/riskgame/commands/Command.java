package riskgame.commands;

/**
 * Commands that need to be made:
 * Attack(Territory from, Territory to)
 * MoveTroops(Country from, Country to)
 * TurnInRiskCard(Player)
 * TakeContinent - could be a subcommand
 * LoseTroops - could be a subcommand
 * <p>
 * <p>
 * When you get full control of a continent you get a certain number of troops
 * <p>
 * Implemented:
 * DraftOneArmy(
 */
public interface Command {
    /**
     * Logs the action of the command
     */
    public void log();

    /**
     * Can also be used a redo. Cannot have any randomization code within the method. ANy
     * randomization must be placed in the Constructor.
     *
     * @throws IllegalExecutionException
     */
    public void execute() throws IllegalExecutionException;

    /**
     * Reverses actions taken by the execute() method
     *
     * @throws IllegalUndoException
     */
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
