import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NormalDie implements Die {

    @Override
    public ArrayList roll(int numDieToRoll) {
        ArrayList numbersRolledList = new ArrayList();
        for (int i = 0; i < numDieToRoll; i++){
            numbersRolledList.add((int)(Math.random()*6+1));
        }
        return numbersRolledList;
    }
}
