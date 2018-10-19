package riskgame.gameobject.player;

public class CreditCardPrompt extends Throwable {
    public final int neededCredit;
    public final int actualCredit;
    public final ProxyCredit credit;

    public CreditCardPrompt(int neededCredit, int actualCredit, ProxyCredit credit) {
        this.neededCredit = neededCredit;
        this.actualCredit = actualCredit;
        this.credit = credit;
    }
}
