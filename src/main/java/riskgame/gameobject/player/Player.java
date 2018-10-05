package riskgame.gameobject.player;

import riskgame.gameobject.RiskCard;
import riskgame.gameobject.Territory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player implements Serializable {
    String name;
    List<RiskCard> hand;
    Set<Territory> territories= new HashSet<Territory>();

    public Player(String name) {
        this.name = name;
    }

    public Player() {

    }

    public void addTerritory(Territory newTerritory) {
        territories.add(newTerritory);
    }

    public void removeTerritory(Territory territory) {
        territories.remove(territory);
    }

    public String getName() {
        return name;
    }
}
