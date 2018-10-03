package riskgame.gameobject;

import riskgame.gameobject.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RiskCard {
    public enum RISK_CARD_TYPE {
        INFANTRY, CAVALRY, ARTILLERY, WILD
    }
    //this assures that each card can only be of one type
    final RISK_CARD_TYPE type;
    Territory territory;
    Player cardOwner;

    public RiskCard(Territory territory, RISK_CARD_TYPE type) {
        this.territory = territory;
        this.type = type;
        this.cardOwner = null;
    }

    public RISK_CARD_TYPE getType(){ return this.type;}
    public Player getCardOwner(){return this.cardOwner;}
    public void setCardOwner(Player cardOwner){this.cardOwner = cardOwner;}

    public static class IllegalCardTradeException extends  Exception{
        public IllegalCardTradeException() {
            super("");
        }
    }

    public static class didntTurnInThreeCardsException extends Exception{
        public didntTurnInThreeCardsException(){
            super("Must trade in 3 cards at a time.");
        }
    }




}