import java.util.*;

public class Main {
    public static void main(String[] args) {
        Territory t = new Territory();
        List<String> territoryList = Arrays.asList("T1", "T2", "T3", "T4", "T5");

        NormalDie die = new NormalDie();
        System.out.println(die.roll(40));
    }

}
