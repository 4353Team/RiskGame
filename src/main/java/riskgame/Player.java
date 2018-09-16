package riskgame;

import java.util.List;

public class Player {
    String name;
    List<RiskCard> hand;

    public void attack(Territory attackingTerritory, Territory defendingTerritory) throws Territory.SelfAttackException {
        attackingTerritory.attack(defendingTerritory);
        //TODO: check if 'attackingTerritory' is bordering 'defendingTerritory', if not throw exception (@Victor's idea)

//        this.roll() // our roll
//        defendingTerritory.controlledBy.roll() // other person's roll
        // ToDo: compare rolls - Victor

    }
}
