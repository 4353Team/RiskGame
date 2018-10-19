package riskgame.gameobject.player;

public class Credit implements PlayerCredit{
    int totalCredit;

    public Credit(int startingCredit) {
        totalCredit = startingCredit;
    }

    @Override
    public void removeCredit(int numToRemove) throws NotEnoughCreditException {
        if (numToRemove > totalCredit) throw new NotEnoughCreditException(numToRemove, totalCredit);
        else {
            totalCredit = totalCredit - numToRemove;
        }
    }

    @Override
    public void addCredit(int numToAdd) {
        totalCredit = totalCredit + numToAdd;
    }

    @Override
    public int getNumCredit() {
        return totalCredit;
    }
}
