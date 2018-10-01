package riskgame.gameobject;

public class MoveTroopsToOwn extends  MoveTroops {
    public MoveTroopsToOwn(Territory newOriginTerritory, Territory newDestinationTerritory, int numArmies) {
        super(newOriginTerritory, newDestinationTerritory, numArmies);
    }
    @Override
    public void execute() throws IllegalExecutionException {
        // assures that player leaves at least one troop behind
        if (originTerritory.getArmies() - numArmies < 1)
            throw new IllegalExecutionException(new InvalidArmyNumberException());
        // assures that player does not try to move troops to a different territory
        if (originTerritory.getControlledBy() != destinationTerritory.getControlledBy())
            throw new IllegalExecutionException(new InvalidTerritoryOwnerException());
        super.execute();
    }
}
