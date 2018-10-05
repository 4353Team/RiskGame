package riskgame.commands;

import java.util.Stack;

public class CommandManager {
    private static boolean log = true;
    private Stack<Command> undos = new Stack<Command>(); // will be the history for the entire game at the end
    private Stack<Command> redos = new Stack<Command>();

    public CommandManager() {
    }

    public void executeCommand(Command command) throws Command.IllegalExecutionException {
        command.execute();
        if (log) command.log();
        undos.push(command);
        redos.clear();
    }

    public boolean isUndoAvailable() {
        return !undos.empty();
    }

    public boolean isRedoAvailable() {
        return !redos.empty();
    }

    public void undo() throws Command.IllegalUndoException {
        assert (isUndoAvailable());
        Command command = undos.pop();
        command.undo();
        redos.push(command);
    }

    public void redo() throws Command.IllegalExecutionException {
        assert (isRedoAvailable());
        Command command = redos.pop();
        command.execute();
        undos.push(command);
    }
}
