package riskgame.gameobject.player;

/**
 * Credit can be held by the player. Credit can be added or removed in case of a purchase.
 */
public interface PlayerCredit {
    public void removeCredit(int numToRemove) throws NotEnoughCreditException, CreditCardPrompt;
    public void addCredit(int numToAdd);
    public int getNumCredit();
}
