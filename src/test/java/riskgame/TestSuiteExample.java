package riskgame;

import org.testng.annotations.Test;
import riskgame.gameobject.Player;
import riskgame.gameobject.Territory;

public class TestSuiteExample {
    @Test
    public void test1() {
        assert 1 == 1;
    }

    @Test
    public void selfAttackExceptionTest() {
        Player p1 = new Player();
        Territory britain = new Territory(p1);
        Exception test = null;

        try {
            p1.attack(britain, britain);
        } catch (Territory.SelfAttackException e) {
            test = e;
            System.out.println(e);
            System.out.println("You may not attack yourself, test successful");
        }

        assert test instanceof Territory.SelfAttackException;
    }
}
