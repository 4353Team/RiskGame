import java.util.ArrayList;
import java.util.List;

public class Territory {
    String name;
    List<Territory> neighbors = new ArrayList<Territory>();
    Player controlledBy;
    List<Army> armies = new ArrayList<Army>();

    public Territory(Player p1) {
        controlledBy = p1;
    }

    //When for example:
    public void addArmies(List armies){
        this.armies.addAll(armies);
    }
    public void attack(Territory territory) throws SelfAttackException {
        if (territory.getControlledBy() == this.controlledBy) {
            throw new SelfAttackException();
        }
    } // return type to be determined

    private Player getControlledBy() {
        return this.controlledBy;
    }

    public class SelfAttackException extends RiskException{

    }

}