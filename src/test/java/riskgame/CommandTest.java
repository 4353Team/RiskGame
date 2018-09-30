package riskgame;

import org.testng.annotations.Test;
import riskgame.commands.Command;
import riskgame.commands.CommandManager;
import riskgame.gameobject.MoveTroops;
import riskgame.gameobject.MoveTroopsToEnemy;
import riskgame.gameobject.MoveTroopsToOwn;
import riskgame.gameobject.player.Player;
import riskgame.gameobject.Territory;

import static org.testng.Assert.assertTrue;

public class CommandTest {

    @Test
    public void DraftTest_1() throws Exception{
        Player player = new Player();
        Territory t = new Territory(player);

        Command command = new Territory.DraftOneArmy(t);

        CommandManager commandManager = new CommandManager();

        commandManager.executeCommand(command);
        assertTrue(t.getArmies() == 1);
        commandManager.undo();
        assertTrue(t.getArmies() == 0);
    }

    @Test
    public void DraftTest_2() throws Exception {
        Player player = new Player();
        Territory t = new Territory(player);

        Command command = new Territory.DraftOneArmy(t);

        CommandManager commandManager = new CommandManager();

        commandManager.executeCommand(command);
        commandManager.executeCommand(command);
        commandManager.executeCommand(command);
        assertTrue(t.getArmies() == 3);
        commandManager.undo();
        assertTrue(t.getArmies() == 2);
    }

    @Test
    public void AttackTest_selfAttack() throws Exception {
        Player attacker = new Player();
        Player defender = new Player();

        Territory territoryA = new Territory(attacker, "Sweden");
        int numArmiesA = 5;
        territoryA.addArmies(numArmiesA);
        Territory territoryD = new Territory(attacker, "Finland");
        int numArmiesD = 1;
        territoryD.addArmies(numArmiesD);

        Command command = new Territory.Attack(territoryA, territoryD);

        CommandManager commandManager = new CommandManager();

        boolean flag = false;
        try {
            commandManager.executeCommand(command);
        } catch (Command.IllegalExecutionException e) {
            flag = true;
            System.out.println(e);
            assertTrue(e.getCause() instanceof Territory.SelfAttackException);
        }
        assertTrue(flag);
    }

    /**
     * Depends on hardcoded movetroops in Territory.Attack
     * @throws Exception
     */
    @Test
    public void AttackTest_1() throws Exception {
        Player attacker = new Player();
        Player defender = new Player();

        Territory territoryA = new Territory(attacker, "Sweden");
        int numArmiesA = 5;
        territoryA.addArmies(numArmiesA);
        Territory territoryD = new Territory(defender, "Finland");
        int numArmiesD = 1;
        territoryD.addArmies(numArmiesD);

        Command command = new Territory.Attack(territoryA, territoryD);

        CommandManager commandManager = new CommandManager();

        commandManager.executeCommand(command);
        assertTrue(territoryA.getArmies() < numArmiesA || territoryD.getArmies() < numArmiesD);
        commandManager.undo();
        assertTrue(territoryA.getArmies() == numArmiesA);
        assertTrue(territoryD.getArmies() == numArmiesD);
        assertTrue(territoryD.getControlledBy() == defender);
    }
    @Test
    public void MoveTroopsTest() throws Exception {
        Player attacker = new Player();
        Player defender = new Player();
        int armiesToMove = 2;
        Territory territoryA = new Territory(attacker, "Argentina");
        int numArmiesA = 5;
        territoryA.addArmies(numArmiesA);
        Territory territoryD = new Territory(defender, "Denmark");
        int numArmiesD = 4;
        territoryD.addArmies(numArmiesD);

        Territory territoryC = new Territory(attacker, "China");
        int numArmiesC = 3;
        territoryC.addArmies(numArmiesC);

        Command command1 = new MoveTroopsToEnemy(territoryA,territoryD,armiesToMove);
        CommandManager commandManager = new CommandManager();
        commandManager.executeCommand(command1);
        assertTrue(territoryA.getArmies() == numArmiesA-armiesToMove);
        assertTrue(territoryD.getArmies() == numArmiesD+armiesToMove);

        Command command2 = new MoveTroopsToOwn(territoryA,territoryC,armiesToMove);
        commandManager = new CommandManager();
        commandManager.executeCommand(command2);
        assertTrue(territoryA.getArmies() == 1);
        assertTrue(territoryC.getArmies() == 5);
        System.out.println(territoryA.getArmies());
        System.out.println(territoryD.getArmies());
        System.out.println(territoryC.getArmies());
        commandManager.undo();
        System.out.println(territoryA.getArmies());
        System.out.println(territoryD.getArmies());
        System.out.println(territoryC.getArmies());



    }
}
