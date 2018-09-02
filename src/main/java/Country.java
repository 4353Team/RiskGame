import java.util.ArrayList;
import java.util.List;

public class Country {
    String name;
    List<Country> neighbors = new ArrayList<Country>();
    Player controlledBy;
    int numArmies;
}
