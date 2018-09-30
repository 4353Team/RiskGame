package riskgame;

import org.testng.annotations.Test;
import riskgame.commands.Command;
import riskgame.commands.CommandManager;
import riskgame.commands.DraftOneArmy;
import riskgame.gameobject.Player;
import riskgame.gameobject.Territory;

import static org.testng.Assert.assertTrue;

public class CommandTest {

    @Test
    public void DraftTest_1() {
        Player player = new Player();
        Territory t = new Territory(player);

        Command command = new DraftOneArmy(t);

        CommandManager commandManager = new CommandManager();

        commandManager.executeCommand(command);
        assertTrue(t.getArmies() == 1);
        commandManager.undo();
        assertTrue(t.getArmies() == 0);
    }

    @Test
    public void DraftTest_2() {
        Player player = new Player();
        Territory t = new Territory(player);

        Command command = new DraftOneArmy(t);

        CommandManager commandManager = new CommandManager();

        commandManager.executeCommand(command);
        commandManager.executeCommand(command);
        commandManager.executeCommand(command);
        assertTrue(t.getArmies() == 3);
        commandManager.undo();
        assertTrue(t.getArmies() == 2);
    }
}
