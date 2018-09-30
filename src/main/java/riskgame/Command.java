package riskgame;

public interface Command {
    // can also be used as a 'redo'
    public void execute();

    //reverses every action take by the execute method
    public void undo();
}
