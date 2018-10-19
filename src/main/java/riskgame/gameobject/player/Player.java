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
    public class GiveCard implements Command {
        // things that will change:
        private Player playerToGive;
        private RiskCard card;

        GiveCard(Player playerToGive, RiskCard card){

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
}
