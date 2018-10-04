package riskgame;

import riskgame.commands.Command;
import riskgame.gameobject.Territory;
import riskgame.ui.UI;

import java.util.List;

public interface GameEngine {
    public void addUi(UI ui);
    public void start() throws Command.IllegalExecutionException, Exception;
    public void undo() throws Exception;
    public void redo() throws Exception;

    List<Territory> getMap();
}
