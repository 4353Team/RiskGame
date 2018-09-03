import org.testng.annotations.Test;

public class TestSuiteExample {
    @Test
    public void test1() {
        assert 1 == 1;
    }

    @Test
    public void selfAttackExceptionTest() {
        Territory britain = new Territory();
        Exception test = null;

        try {
            britain.attack(britain);
        } catch (Territory.SelfAttackException e) {
            test = e;
            System.out.println("You may not attack yourself, test successful");
        }

        assert test instanceof Territory.SelfAttackException;
    }
}
