package riskgame.gameobject.player;

import riskgame.commands.Command;
import riskgame.gameobject.RiskCard;
import riskgame.gameobject.Territory;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private String name;
    private Set<Territory> ownedTerritories = new HashSet<Territory>();
    private List<RiskCard> riskCards;
    private int totalArmies;
    private int undeployedArmies;
    int numRiskCardSetsTradedIn;

    /**
     * returns a copy of owned Territories to prevent mutation
     * @return
     */
    public Set<Territory> getOwnedTerritories(){
        return this.ownedTerritories.stream().collect(Collectors.toSet());
    }
    public void setOwnedTerritories(Set<Territory> ownedTerritories){this.ownedTerritories = ownedTerritories;}

    public List<RiskCard> getRiskCards(){
        List<RiskCard> riskCards = new ArrayList<RiskCard>();
        Collections.copy(riskCards, this.riskCards);
        return riskCards;
    }
    public void setRiskCards(List<RiskCard> riskCards){this.riskCards = riskCards;}

    public int getTotalArmies(){return totalArmies;}
    public void setTotalArmies(int totalArmies){this.totalArmies = totalArmies;}
    public int getUndeployedArmies(){return undeployedArmies;}
    public void setUndeployedArmies(int undeployedArmies) {this.undeployedArmies = undeployedArmies;}

    public void addTerritory(Territory newTerritory) {
        ownedTerritories.add(newTerritory);
    }

    public void removeTerritory(Territory territory) {
        ownedTerritories.remove(territory);
    }


    /**
     *
     * Pre-conditions:
     * -> at end of turn
     * ->& player has captured at least one territory
     *
     * Post-condition: player earns at most one risk card
     * player earns armies based on that earnedCard and possibly bonus armies if player owns territory on card
     */
    public void earnCard(){

    }

    /**
     * * Valid Combinations:
     *      * -> 3 cards of the same design
     *      * -> 1 of each of 3 designs
     *      * -> any 2 designs plus a "wild card"
     * @param threeCards
     * @return
     * @throws Exception
     */
    public boolean isValidCardCombination(List<RiskCard> threeCards) throws Exception{
        if (threeCards.size() != 3)
            throw new RiskCard.didntTurnInThreeCardsException();

        List<Boolean> cardsSeen = new ArrayList<Boolean>();
        Collections.addAll(cardsSeen,false,false,false,false);
        List<List<Boolean>> validCardCombinations = new ArrayList<List<Boolean>>();

         /*
        i, c, a, w
        1  1  1  0
        1  0  0  0

        1  1  0  1
        1  0  1  1
        0  1  1  1
        */

        //1 of each 3 designs
        List<Boolean> comb1 = new ArrayList<>();
        Collections.addAll(comb1, true, true, true, false);
        // cards of same design (3 infantry, Calvary of Artillery)
        List<Boolean> comb2 = new ArrayList<>();
        Collections.addAll(comb2,true,false,false,false);
        //Any 2 plus a "wild card"
        List<Boolean> comb3 = new ArrayList<>();
        Collections.addAll(comb3,true,true,false,true);
        List<Boolean> comb4 = new ArrayList<>();
        Collections.addAll(comb4, true, false, true, true);
        List<Boolean> comb5 = new ArrayList<>();
        Collections.addAll(comb5,false,true,true,true);

        Collections.addAll(validCardCombinations, comb1, comb2, comb3,comb4,comb5);


        for (RiskCard card : threeCards) {
            switch (card.getType()) {
                case INFANTRY:
                    cardsSeen.set(0, true);
                    break;
                case CAVALRY:
                    cardsSeen.set(1, true);
                    break;
                case ARTILLERY:
                    cardsSeen.set(2, true);
                    break;
                case WILD:
                    cardsSeen.set(3, true);
                    break;
            }
        }
        for (int i = 0; i < validCardCombinations.size(); i++) {
            if (cardsSeen.equals(validCardCombinations.get(i))){
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    private static class TurnInRiskCardsCommand implements Command {
        private Player player;
        private final int previousUndeployedArmies;
        private final int previousTotalArmies;
        private final List<RiskCard> previousRiskCards;
        private final List<RiskCard> turnedInCards;

        private TurnInRiskCardsCommand(Player p, List<RiskCard> turnedInCards) {
            this.player = p;
            this.previousRiskCards = p.riskCards;
            this.turnedInCards = turnedInCards;
            this.previousTotalArmies = p.totalArmies;
            this.previousUndeployedArmies = p.undeployedArmies;
        }

        public void execute() throws IllegalExecutionException{

        }

        public void undo() {
            player.setRiskCards(previousRiskCards);
            player.setTotalArmies(previousTotalArmies);
            player.setUndeployedArmies(previousUndeployedArmies);
        }




    }
}
