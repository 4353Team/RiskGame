package riskgame.ui;

import riskgame.GameEngine;
import riskgame.GameMaps;
import riskgame.SingleUIGame;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.CreditCardPrompt;
import riskgame.gameobject.player.NotEnoughCreditException;
import riskgame.gameobject.player.Player;

import java.util.List;
import java.util.Map;

public class TestUI implements UI {
    private GameEngine gameEngine;
    private List<Player> playerSetup;

    public TestUI(List<Player> playerSetup) {
        this.playerSetup = playerSetup;
    }

    @Override
    public void addGame(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public List<Territory> map;
    @Override
    public void update() {
        map = gameEngine.getMap();
    }

    @Override
    public GameMaps.GameMap selectMap(GameMaps gameMaps) {
        for (GameMaps.GameMap map:gameMaps.getList()) {
            if (map.getName().equals(expectedMap)) return map;
        }
        throw new AssertionError("Expected Map Not Found");
    }

    public Exception exception;

    /**
     * display exception to player
     *
     * @param e the Exception
     */
    @Override
    public void error(Exception e) {
        System.out.println("There was an error! " + e);
        exception = e;
    }

    /**
     * implementation is up to the UI. First player in the returned list goes first.
     *
     * @return List of configured players
     */
    @Override
    public List<Player> selectAndNamePlayers() {
        return playerSetup;
    }

    /**
     * Only happens during the initial stage, Player currentPlayer picks where to draft their army one at a time.
     *
     * @param currentPlayer the Player whose turn it is to pick
     * @param territories
     * @return the Territory the Player picked to add an army to
     */
    @Override
    public Territory getInitDraftPick(Player currentPlayer, List<Territory> territories) {
        int index = playerSetup.indexOf(currentPlayer);
        return map.get(index);
    }

    @Override
    public Map.Entry<Territory, Integer> getDraftPick(Player currentPlayer, List<Territory> territories) {
        return null;
    }

    /**
     * always buys 5 more credits than needed
     * @param creditCardPrompt the data needed to show the user
     * @return
     * @throws CreditPromptCancelledException
     */
    @Override
    public int creditCardPrompt(CreditCardPrompt creditCardPrompt) throws CreditPromptCancelledException {
        return 5 + creditCardPrompt.neededCredit;
    }

    @Override
    public void notEnoughCredit(NotEnoughCreditException e) {
        System.out.println("Not enough credit, need: " + e.creditNeeded + " but have " + e.actualCredit);
    }

    @Override
    public Territory.AttackPick getAttackPick(Player currentPlayer, List<Territory> territories) throws NoMoreAttackException {
        return null;
    }

    @Override
    public int queryArmiesToMove(Player currentPlayer, Territory from, Territory to) {
        return 0;
    }

    @Override
    public void tellPlayersToClaimTheirFirstTerritories() {

    }

    @Override
    public String askPlayerIfToDraft(Player currentPlayer) {
        return null;
    }

    @Override
    public String askPlayerIfToAttack(Player currentPlayer) {
        return null;
    }

    @Override
    public SingleUIGame.FortifyPick getFortifyPick(Player currentPlayer) {
        return null;
    }

    private String expectedMap;
    public void expectMap(String territoryStraightLine) {
        expectedMap = territoryStraightLine;
    }
}
