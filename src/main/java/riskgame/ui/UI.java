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

/**
 * essentially an Observer (design pattern) with the ability to prompt
 */
public interface UI {
    public void addGame(GameEngine gameEngine);
    public void update();

    public GameMaps.GameMap selectMap(GameMaps gameMaps);

    /**
     * display exception to player
     * @param e the Exception
     */
    public void error(Exception e);

    /**
     * implementation is up to the UI. First player in the returned list goes first.
     * @return List of configured players
     */
    List<Player> selectAndNamePlayers();

    /**
     * Only happens during the initial stage, Player currentPlayer picks where to draft their army one at a time.
     * @param currentPlayer the Player whose turn it is to pick
     * @param territories
     * @return the Territory the Player picked to add an army to
     */
    Territory getInitDraftPick(Player currentPlayer, List<Territory> territories);
    Map.Entry<Territory, Integer> getDraftPick(Player currentPlayer, List<Territory> territories);

    /**
     * This should prompt the user with a window containing one input,
     * a textbox where an integer will be input as to how many credits to buy.
     * Do not implement any actual credit card functionality.
     *
     * @param creditCardPrompt the data needed to show the user
     * @return the number of credits to add
     */
    int creditCardPrompt(CreditCardPrompt creditCardPrompt) throws CreditPromptCancelledException;

    void notEnoughCredit(NotEnoughCreditException e);

    Territory.AttackPick getAttackPick(Player currentPlayer) throws NoMoreAttackException;

    int queryArmiesToMove(Player currentPlayer, Territory from, Territory to);

    void tellPlayersToClaimTheirFirstTerritories();
    String askPlayerIfWantToDraft(Player currentPlayer);

    SingleUIGame.FortifyPick getFortifyPick(Player currentPlayer);

    class CreditPromptCancelledException extends Exception { }

    class NoMoreAttackException extends Exception {
    }
}
