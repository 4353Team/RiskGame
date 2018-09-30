package riskgame.gameobject.player;

import riskgame.gameobject.RiskCard;
import riskgame.gameobject.Territory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    String name;
    List<RiskCard> hand;
    Set<Territory> territories= new HashSet<Territory>();

    public void addTerritory(Territory newTerritory) {
        territories.add(newTerritory);
    }

    public void removeTerritory(Territory territory) {
        territories.remove(territory);
    }
}
