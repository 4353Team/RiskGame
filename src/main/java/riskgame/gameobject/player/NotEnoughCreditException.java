package riskgame.gameobject.player;

public class NotEnoughCreditException extends Throwable {
    public final int creditNeeded;
    public final int actualCredit;
    public NotEnoughCreditException(int numToRemove, int totalCredit) {
        creditNeeded = numToRemove;
        actualCredit = totalCredit;
    }
}
