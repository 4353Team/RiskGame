package riskgame.commands;

/**
 * Commands that need to be made:
 * Attack
 * MoveTroops
 * TurnInRiskCard
 *
 * When you get full control of a continent you get a certain number of troops
 */
public interface Command {
    // can also be used as a 'redo'
    public void execute();

    //reverses every action take by the execute method
    public void undo();
}
