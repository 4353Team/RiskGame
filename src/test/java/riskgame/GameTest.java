package riskgame;


import com.amazonaws.annotation.NotThreadSafe;
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

@NotThreadSafe
public class GameTest {
    private static final Logger logger = LogManager.getLogger(GameTest.class);

    @Test(enabled = false)
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
                        "5\n" + //35
                        "2\n" + //34
                        "3\n" + //33
                        "1\n" + //32
                        "4\n" + //31
                        "9\n" + //30
                        "7\n" + //29
                        "8\n" + //28
                        "6\n" + //27
                        //place more armies in already claimed territories
                        //3 players means 35 armies
                        //30 armies left
                        "5\n" + //26
                        "2\n" + //25
                        "3\n" + //24
                        "1\n" + //23
                        "4\n" + //22
                        "9\n" + //21
                        "7\n" + //20
                        "8\n" + //19
                        "6\n" + //18
                        "5\n" + //17
                        "2\n" + //16
                        "3\n" + //15
                        "1\n" + //14
                        "4\n" + //13
                        "9\n" + //12
                        "7\n" + //11
                        "8\n" + //10
                        "6\n" + //09
                        "5\n" + //08
                        "2\n" + //07
                        "3\n" + //06
                        "1\n" + //05
                        "4\n" + //04
                        "9\n" + //03
                        "7\n" + //02
                        "8\n" + //01
                        //
                        "Y\n" + //first player chooses to draft
                        "7\n" + //select territory to draft to
                        "1\n" + //draft 1 army
                        // tries to attack self
                        "Y\n" +
                        "7\n" +
                        "1\n" +
                        // first attack
                        "Y\n" +
                        "7\n" +
                        "9\n" +
                        "END\n"
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

    @Test(enabled = true)
    public void dummyRealGame_3Players_gotofortify() throws Exception {
//        Scanner scanner = new Scanner(System.in);
        BufferedReader bufferedReader = new BufferedReader(new StringReader(
                "3\n" + //choose map SMALL_WORLD
                        "3\n" + // number of players
                        "Kesha\n" +
                        "Ana\n" +
                        "Elizabeth\n " +
                        //claiming territories
                        "5\n" + //35
                        "2\n" + //34
                        "3\n" + //33
                        "1\n" + //32
                        "4\n" + //31
                        "9\n" + //30
                        "7\n" + //29
                        "8\n" + //28
                        "6\n" + //27
                        //place more armies in already claimed territories
                        //3 players means 35 armies
                        //30 armies left
                        "5\n" + //26
                        "2\n" + //25
                        "3\n" + //24
                        "1\n" + //23
                        "4\n" + //22
                        "9\n" + //21
                        "7\n" + //20
                        "8\n" + //19
                        "6\n" + //18
                        "5\n" + //17
                        "2\n" + //16
                        "3\n" + //15
                        "1\n" + //14
                        "4\n" + //13
                        "9\n" + //12
                        "7\n" + //11
                        "8\n" + //10
                        "6\n" + //09
                        "5\n" + //08
                        "2\n" + //07
                        "3\n" + //06
                        "1\n" + //05
                        "4\n" + //04
                        "9\n" + //03
                        "7\n" + //02
                        "8\n" + //01
                        //
                        "Y\n" + //first player chooses to draft
                        "7\n" + //select territory to draft to
                        "1\n" + //draft 1 army
                        // tries to attack self
                        "Y\n" +
                        "7\n" +
                        "1\n" +
                        // first attack
                        "N\n" +
                        //fortify
                        "5\n" + //select a territory to move troops from
                        "5\n" + //tries to move army to same territory its already on
                        "100\n" +
                        "5\n" + //select a territory to move troops from
                        "1\n" + //select a territory to move troops to
                        "1\n" + //number of troops to move
                        //DRAFT 2
                        "Y\n" +
                        "4\n" +
                        "1\n" + //armies to draft to territory 4
                        "END\n"
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
