package riskgame.gameobject.player;

public class ProxyCredit implements PlayerCredit{
    final PlayerCredit realCredit;

    ProxyCredit(int creditCount){
        realCredit = new Credit(creditCount);
    }

    @Override
    public void removeCredit(int numToRemove) throws NotEnoughCreditException, CreditCardPrompt {
        try {
            realCredit.removeCredit(numToRemove);
        } catch (NotEnoughCreditException e) {
            throw new CreditCardPrompt(numToRemove, realCredit.getNumCredit(), this);
        }
    }

    @Override
    public void addCredit(int numToAdd) {
        realCredit.addCredit(numToAdd);
    }

    @Override
    public int getNumCredit() {
        return realCredit.getNumCredit();
    }
}
