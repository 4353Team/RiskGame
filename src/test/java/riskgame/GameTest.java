package riskgame;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import riskgame.amazons3.AmazonS3;
import riskgame.commands.Command;
import riskgame.commands.CommandManager;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.HumanPlayer;
import riskgame.gameobject.player.Player;
import riskgame.ui.TestUI;
import riskgame.ui.TextUI;
import riskgame.ui.UI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.testng.Assert.assertTrue;

public class GameTest {
    private static final Logger logger = LogManager.getLogger(GameTest.class);

    @Test
    public void dummyUiGame_3_players() throws Exception {
        List<Player> playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("Player 1"));
        playerList.add(new HumanPlayer("Player 2"));
        playerList.add(new HumanPlayer("Player 3"));


        TestUI testUI = new TestUI(playerList);
        testUI.expectMap(GameMaps.TERRITORY_STRAIGHT_LINE);

        GameEngine gameEngine = new SingleUIGame();

        gameEngine.addUi(testUI);
        testUI.addGame(gameEngine);

        gameEngine.start();

        assertTrue(testUI.map.get(1).getControlledBy() == playerList.get(1));
    }

    @Test
    public void dummyRealGame_3Players() throws Exception {
//        Scanner scanner = new Scanner(System.in);
        Scanner scanner = new Scanner(new StringReader(
                "3\n" + // SMALL_WORLD
                        "3\n" + // number of players
                        "Kesha\n" +
                        "Ana\n" +
                        "Elizabeth\n " +
                        "1\n" +
                        "2\n" +
                        "3\n"));

        GameEngine gameEngine = new SingleUIGame();
        gameEngine.disableCommandLogs();

        //
        TextUI textUI = new TextUI(System.out, scanner);
        gameEngine.addUi(textUI);
        textUI.addGame(gameEngine);

        gameEngine.start();
        assertTrue(true);
    }

    @Test(enabled = false)
    public void amazonTest() throws Exception {
        Player player = new Player();
        Territory t = new Territory(player);
        Command command = new Territory.DraftOneArmy(t);

        CommandManager commandManager = new CommandManager();

        commandManager.executeCommand(command);
        commandManager.executeCommand(command);
        commandManager.executeCommand(command);
        commandManager.undo();
        AmazonS3 as3 = new AmazonS3(commandManager);
        as3.saveCommandManagerState();
        String bucketId = as3.saveFileToAmazon();

        CommandManager myCommandManager = as3.readCommandManagerState();
        assertTrue(commandManager.getUndosCount() == 2);
        assertTrue(commandManager.getRedosCount() == 1);
    }

}
