package riskgame.gameobject;

import riskgame.commands.Command;

public class MoveTroops implements Command {

    protected Territory originTerritory;
    protected Territory destinationTerritory;

    protected int numArmies;

    public MoveTroops(Territory newOriginTerritory, Territory newDestinationTerritory, int numArmies) {
        this.originTerritory = newOriginTerritory;
        this.destinationTerritory = newDestinationTerritory;
        this.numArmies = numArmies;
    }
    @Override
    public void execute() throws IllegalExecutionException {
        originTerritory.removeArmies(numArmies);
        destinationTerritory.addArmies(numArmies);
    }
    @Override
    public void undo() throws IllegalUndoException {
        destinationTerritory.removeArmies(numArmies);
        originTerritory.addArmies(numArmies);
    }
    protected static class InvalidArmyNumberException extends Throwable {
    }
    protected static class InvalidTerritoryOwnerException extends Throwable {
    }
}