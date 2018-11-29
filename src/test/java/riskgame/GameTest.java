package riskgame;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.facilities.TelegramHttpClientBuilder;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
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

import static org.testng.Assert.*;

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

        assertSame(testUI.map.get(1).getControlledBy(), playerList.get(1));
    }


    @Test(enabled = true)
    public void dummyRealGame_3Players() throws Exception {
//        Scanner scanner = new Scanner(System.in);
        BufferedReader bufferedReader = new BufferedReader(new StringReader(
                "3\n" + //choose map SMALL_WORLD
                        "3\n" + // number of players
                        "Kesha\n" +
                        "Ana\n" +
                        "Elizabeth\n " +
                        //claiming territories
                        "5\n" +
                        "2\n" +
                        "3\n" +
                        "1\n" +
                        "4\n" +
                        //place more armies in already claimed territories
                        //3 players means 35 armies
                        //30 armies left
                        "3\n" + //30
                        "5\n" + //29
                        "2\n" + //28
                        "3\n" + //27
                        "5\n" + //26
                        "2\n" + //25
                        "3\n" + //24
                        "5\n" + //23
                        "2\n" + //22
                        "3\n" + //21
                        "5\n" + //20
                        "2\n" + //19
                        "3\n" + //18
                        "5\n" + //17
                        "2\n" + //16
                        "3\n" + //15
                        "5\n" + //14
                        "2\n" + //13
                        "3\n" + //12
                        "5\n" + //11
                        "2\n" + //10
                        "3\n" + //09
                        "5\n" + //08
                        "2\n" + //07
                        "3\n" + //06
                        "5\n" + //05
                        "2\n" + //04
                        "3\n" + //03
                        "5\n" + //02
                        "2\n" + //01
                        //
                        ""
                ));

        GameEngine gameEngine = new SingleUIGame();
        gameEngine.disableCommandLogs();

        //
        TextUI textUI = new TextUI(System.out, bufferedReader);
        gameEngine.addUi(textUI);
        textUI.addGame(gameEngine);

        gameEngine.start();
        assertTrue(true);
    }

    @Test
    public void telegramGame() throws Exception {
        // Initialize Api Context
        ApiContextInitializer.init();

        JavaRiskGameBot bot = new JavaRiskGameBot();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register our bot
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        UI textUi = new TextUI(bot.getWriter(), bot.getReader());
        SingleUIGame game = new SingleUIGame();
        game.addUi(textUi);


        new Thread(() -> {
            try {
                game.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Test(enabled = false)
    public void tryThis() throws IOException, InterruptedException {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        final PipedInputStream in = new PipedInputStream(pipedOutputStream);

        PrintStream writer = new PrintStream(pipedOutputStream, true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                try {
                    for (String line; (line = reader.readLine()) != null; ) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        for (int i = 0; i < 1000; i++) {
            writer.println(i);
            Thread.sleep(1000);
        }
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
        assertEquals(2, commandManager.getUndosCount());
        assertEquals(1, commandManager.getRedosCount());
    }

}
