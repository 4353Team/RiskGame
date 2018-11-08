package riskgame;

import riskgame.commands.Command;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.CreditCardPrompt;
import riskgame.gameobject.player.NotEnoughCreditException;
import riskgame.gameobject.player.Player;
import riskgame.ui.UI;

import java.util.List;

public interface GameEngine {
    public void addUi(UI ui);
    public void start() throws Command.IllegalExecutionException, Exception;
    public void undo() throws Exception;
    public void redo() throws Exception;
    public void buyUndo(Player player) throws NotEnoughCreditException, CreditCardPrompt;
    public void buyRiskCards(Player buyer) throws NotEnoughCreditException, CreditCardPrompt;
    List<Territory> getMap();

    void disableCommandLogs();
}
