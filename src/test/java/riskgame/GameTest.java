package riskgame;

import org.testng.annotations.Test;
import riskgame.gameobject.player.HumanPlayer;
import riskgame.gameobject.player.Player;
import riskgame.ui.TestUI;
import riskgame.ui.UI;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class GameTest {
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
}
