package riskgame.gameobject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import riskgame.commands.Command;

public class MoveTroops implements Command {
    private final static Logger logger = LogManager.getLogger(MoveTroops.class);

    private Territory originTerritory;
    private Territory destinationTerritory;

    private int numArmies;

    public MoveTroops(Territory newOriginTerritory, Territory newDestinationTerritory, int numArmies) {
        this.originTerritory = newOriginTerritory;
        this.destinationTerritory = newDestinationTerritory;
        this.numArmies = numArmies;
    }

    /**
     * Logs the action of the command
     */
    @Override
    public void log() {
        logger.info(numArmies + " troops are being moved from Territory: " + originTerritory + " to Territory: " + destinationTerritory);
    }

    @Override
    public void execute() throws IllegalExecutionException {
        // assures that player leaves at least one troop behind
        if (originTerritory.getArmies() - numArmies < 1)
            throw new IllegalExecutionException(new InvalidArmyNumberException());
        // assures that player does not try to move troops to a different territory
        if (originTerritory.getControlledBy() != destinationTerritory.getControlledBy())
            throw new IllegalExecutionException(new InvalidTerritoryOwnerException());
        originTerritory.removeArmies(numArmies);
        destinationTerritory.addArmies(numArmies);

    }
    @Override
    public void undo() throws IllegalUndoException {
        // assures that player leaves at least one troop behind
        if (destinationTerritory.getArmies() - numArmies < 1)
            throw new IllegalUndoException(new InvalidArmyNumberException());
        // assures that player does not try to move troops to a different territory
        if (destinationTerritory.getControlledBy() != originTerritory.getControlledBy())
            throw new IllegalUndoException(new InvalidTerritoryOwnerException());
        destinationTerritory.removeArmies(numArmies);
        originTerritory.addArmies(numArmies);
    }
    private static class InvalidArmyNumberException extends Throwable {
    }
    private static class InvalidTerritoryOwnerException extends Throwable {
    }

}
//