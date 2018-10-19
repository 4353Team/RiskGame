package riskgame.gameobject.player;

public class ProxyCredit implements PlayerCredit{
    final PlayerCredit realCredit;
    ProxyCredit(int creditCount){
        realCredit = new Credit(creditCount);
    }

    @Override
    public void removeCredit(int numToRemove) throws NotEnoughCreditException {
        try {
            realCredit.removeCredit(numToRemove);
        } catch (NotEnoughCreditException e) {
            // prompt the user for their credit card information if they don't have enough credit in the game
            e.printStackTrace();
            System.out.println("you actually had: " + e.actualCredit + " credit. But you tried to buy something that cost: " + e.creditNeeded + ".... you idiot");
        }
    }

    @Override
    public void addCredit(int numToAdd) {
        realCredit.addCredit(numToAdd);
    }
}
