import java.util.ArrayList;
import java.util.List;

public class Country {
    String name;
    List<Country> neighbors = new ArrayList<Country>();
    Player controlledBy;
    int numArmies;

    public void attack(Country country) throws AttackException {
        if (country.getControlledBy() == this.controlledBy) {
            throw new SelfAttackException();
        }
    } // return type to be determined

    private Player getControlledBy() {
        return this.controlledBy;
    }
}
