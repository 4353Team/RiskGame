package riskgame.gameobject.player;

import org.apache.logging.log4j.LogManager;
import riskgame.SingleUIGame;
import riskgame.commands.Command;
import riskgame.gameobject.RiskCard;
import riskgame.gameobject.Territory;
import riskgame.ui.UI;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

public class Player implements Serializable {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SingleUIGame.class);
    String name;
    List<RiskCard> hand = new ArrayList<>();
    Set<Territory> territories= new HashSet<Territory>();
    PlayerCredit credit = new ProxyCredit(0); // could in theory be set to just regular Credit

    public Player(String name) {
        this.name = name;
    }

    public Player() {

    }

    public Player(String  name, int initialCredit) {
        this.name = name;
        this.credit = new Credit(initialCredit);
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

    public List<RiskCard> getHand() {
        return hand;
    }

    public int getCredit() {
        return credit.getNumCredit();
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

    public static class TransferCredit implements Command {
        // things that will change:
        private Player playerGivingCredit;
        private Player playerReceivingCredit;
        int creditToTransfer;
        UI ui;

        public TransferCredit(Player playerGivingCredit, Player playerReceivingCredit, int creditToTransfer, UI ui){

            this.playerGivingCredit = playerGivingCredit;
            this.playerReceivingCredit = playerReceivingCredit;
            this.creditToTransfer = creditToTransfer;
            this.ui = ui;
        };

        @Override
        public void log() {
            logger.info("Player " + playerGivingCredit.getName() + " has given " + playerReceivingCredit.getName());
        }

        @Override
        public void execute() throws IllegalExecutionException {
            try {
                playerGivingCredit.credit.removeCredit(creditToTransfer);
                playerReceivingCredit.credit.addCredit(creditToTransfer);

            } catch (NotEnoughCreditException e) {
                ui.notEnoughCredit(e);
            } catch (CreditCardPrompt creditCardPrompt) {
                try {
                    //adding credit to game credit
                    int creditToAdd = ui.creditCardPrompt(creditCardPrompt);
                    creditCardPrompt.credit.addCredit(creditToAdd);
                } catch (UI.CreditPromptCancelledException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void undo() throws IllegalUndoException {
            try {
                playerReceivingCredit.credit.removeCredit(creditToTransfer);
                playerGivingCredit.credit.addCredit(creditToTransfer);


            } catch (NotEnoughCreditException e) {
                e.printStackTrace();
            } catch (CreditCardPrompt creditCardPrompt) {
                creditCardPrompt.printStackTrace();
            }
        }
    }
}
