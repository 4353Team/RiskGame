package riskgame;

import riskgame.Command;
import java.util.Stack;

public class CommandManager {
    private Stack<Command> undos = new Stack<Command>();
    private Stack<Command> redos = new Stack<Command>();
    public CommandManager(){}

    public void executeCommand(Command c){
        c.execute();;
        undos.push(c);
        redos.clear();
    }

    public boolean isUndoAvailable(){
        return !undos.empty();
    }

    public boolean isRedoAvailable(){
        return !redos.empty();
    }

    public void undo(){
        assert(isUndoAvailable());
        Command command = undos.pop();
        command.undo();
        redos.push(command);
    }
    public void redo(){
        assert (isRedoAvailable());
        Command command = redos.pop();
        command.execute();
        undos.push(command);
    }
}
