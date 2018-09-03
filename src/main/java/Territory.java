import java.util.ArrayList;
import java.util.List;

public class Territory {
    String name;
    List<Territory> neighbors = new ArrayList<Territory>();
    Player controlledBy;
    int numArmies;

    public void attack(Territory territory) throws SelfAttackException {
        if (territory.getControlledBy() == this.controlledBy) {
            throw new SelfAttackException();
        }
    } // return type to be determined

    private Player getControlledBy() {
        return this.controlledBy;
    }
}
