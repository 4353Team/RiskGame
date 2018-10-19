package riskgame.gameobject.player;

public class NotEnoughCreditException extends Throwable {
    final int creditNeeded;
    final int actualCredit;
    public NotEnoughCreditException(int numToRemove, int totalCredit) {
        creditNeeded = numToRemove;
        actualCredit = totalCredit;
    }
}
