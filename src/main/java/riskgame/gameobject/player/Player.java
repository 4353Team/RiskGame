package riskgame.gameobject.player;

import org.apache.logging.log4j.LogManager;
import riskgame.SingleUIGame;
import riskgame.commands.Command;
import riskgame.gameobject.RiskCard;
import riskgame.gameobject.Territory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Player implements Serializable {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SingleUIGame.class);
    String name;
    List<RiskCard> hand;
    Set<Territory> territories= new HashSet<Territory>();
    PlayerCredit credit = new ProxyCredit(0); // could in theory be set to just regular Credit

    public Player(String name) {
        this.name = name;
    }

    public Player() {

    }

    public void addTerritory(Territory newTerritory) {
        territories.add(newTerritory);
    }

    public void removeTerritory(Territory territory) {
        territories.remove(territory);
    }

    public String getName() {
        return name;
    }

    /**
     * class to give player risk card earned/bought
     */
    public static class GiveCard implements Command {
        // things that will change:
        private Player playerToGive;
        private RiskCard card;

        public GiveCard(Player playerToGive, RiskCard card){

            this.playerToGive = playerToGive;
            this.card = card;
        };

        @Override
        public void log() {
            logger.info("Player " + playerToGive + " has received a risk card: " + card.toString());
        }

        @Override
        public void execute() throws IllegalExecutionException {
            playerToGive.hand.add(card);
        }

        @Override
        public void undo() throws IllegalUndoException {
            playerToGive.hand.remove(card);
        }
    }

    /**
     * just simply contains a giveCardCommand and calls the opposite action on it
     */
    public static class TakeCard implements Command {
        private Command giveCardCommand;

        public TakeCard(Player playerToGive, RiskCard card){
            giveCardCommand = new GiveCard(playerToGive, card);
        };

        @Override
        public void log() {

        }

        @Override
        public void execute() throws IllegalExecutionException {
            try {
                giveCardCommand.undo();
            } catch (IllegalUndoException e) {
                throw new IllegalExecutionException(e);
            }
        }

        @Override
        public void undo() throws IllegalUndoException {
            try {
                giveCardCommand.execute();
            } catch (IllegalExecutionException e) {
                throw new IllegalUndoException(e);
            }
        }
    }
}
