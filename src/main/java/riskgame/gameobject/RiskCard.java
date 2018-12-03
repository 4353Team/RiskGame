package riskgame.gameobject;

import riskgame.gameobject.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RiskCard {
    private Player cardOwner;

    public enum RISK_CARD_TYPE {
        INFANTRY, CAVALRY, ARTILLERY, WILD
    }

    //this assures that each card can only be of one type
    public RiskCard(Territory territory, RISK_CARD_TYPE type) {
        this.territory = territory;
        this.type = type;
        this.cardOwner = null;
    }

    final RISK_CARD_TYPE type;
    Territory territory;

    public RiskCard(RISK_CARD_TYPE type) {
        this.type = type;
    }

    public RISK_CARD_TYPE getType() {
        return this.type;
    }

    public Player getCardOwner() {
        return this.cardOwner;
    }

    public void setCardOwner(Player cardOwner) {
        this.cardOwner = cardOwner;
    }

    public static class IllegalCardTradeException extends Exception {
        public IllegalCardTradeException() {
            super("");
        }
    }

    public static class didntTurnInThreeCardsException extends Exception {
        public didntTurnInThreeCardsException() {
            super("Must trade in 3 cards at a time.");
        }
    }

    @Override
    public String toString() {
        return "type: "+ this.type.name() + "\tterritory: " + this.territory;
    }

    public static Boolean isValidTrade(List<RiskCard>cards){
        // player was forced to turn in exactly three cards 'at a time' @SingleUIGame.java

        //assuming three cards will be traded in!
        RiskCard c1 = cards.get(0);
        RiskCard c2 = cards.get(1);
        RiskCard c3 = cards.get(2);
        /* Valid Trades */

        // 3 cards of same type
        if(c1.getType() == c2.getType() && c2.getType() == c3.getType()) {
            return true;
        }
        // 1 card of each type
        else if(!(c1.getType() == c2.getType()) && !(c1.getType() == c3.getType()) && !(c2.getType() == c3.getType())){
            return true;
        }
        // any two types plus wild card
        else if(c1.getType() == RISK_CARD_TYPE.WILD || c2.getType() == RISK_CARD_TYPE.WILD || c3.getType() == RISK_CARD_TYPE.WILD){
            return true;
        }
        else
            return false;

    }
}