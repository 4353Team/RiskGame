package riskgame.commands;

import riskgame.gameobject.Territory;

public class DraftOneArmy implements Command {
    Territory territory;
    public DraftOneArmy(Territory t) {
        territory = t;
    }

    @Override
    public void execute() {
        territory.addArmies(1);
    }

    @Override
    public void undo() {
        territory.removeArmies(1);
    }
}
