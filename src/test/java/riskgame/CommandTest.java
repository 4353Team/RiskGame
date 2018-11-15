package riskgame;

import org.testng.annotations.Test;
import riskgame.commands.Command;
import riskgame.commands.CommandManager;
import riskgame.gameobject.MoveTroops;
import riskgame.gameobject.RiskCard;
import riskgame.gameobject.player.CreditCardPrompt;
import riskgame.gameobject.player.NotEnoughCreditException;
import riskgame.gameobject.player.Player;
import riskgame.gameobject.Territory;
import riskgame.ui.TestUI;
import riskgame.ui.UI;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

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

        Command command = new Territory.Attack(territoryA, territoryD, new SingleUIGame());

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

        SingleUIGame dummyGame = new SingleUIGame();
        dummyGame.gameState = SingleUIGame.GameState.ATTACK;
        Command command = new Territory.Attack(territoryA, territoryD, dummyGame);

        CommandManager commandManager = new CommandManager();

        commandManager.executeCommand(command);
        assertTrue(territoryA.getArmies() < numArmiesA || territoryD.getArmies() < numArmiesD);
        commandManager.undo();
        assertEquals(territoryA.getArmies(), numArmiesA);
        assertEquals(territoryD.getArmies(), numArmiesD);
        assertSame(territoryD.getControlledBy(), defender);
    }
    @Test
    public void MoveTroopsTest() throws Exception {
        Player attacker = new Player();
        Player defender = new Player();
        int armiesToMove = 1;
        Territory territoryA = new Territory(attacker, "Argentina");
        int numArmiesA = 5;
        territoryA.addArmies(numArmiesA);
        Territory territoryC = new Territory(attacker, "China");
        int numArmiesC = 3;
        territoryC.addArmies(numArmiesC);


        Command command = new MoveTroops(territoryA, territoryC, armiesToMove);
        CommandManager commandManager = new CommandManager();
        commandManager.executeCommand(command);
        assertTrue(territoryA.getArmies() == (numArmiesA-armiesToMove));
        assertTrue(territoryC.getArmies() == (numArmiesC+armiesToMove));
        commandManager.undo();
        assertTrue(territoryA.getArmies() == numArmiesA);
        assertTrue(territoryC.getArmies() == numArmiesC);
    }

    @Test
    public void GiveCard() throws Command.IllegalExecutionException {
        Player player = new Player("ev");
        Territory territory = new Territory("Russia");
        RiskCard card = new RiskCard(territory, RiskCard.RISK_CARD_TYPE.CAVALRY);
        Command command = new Player.GiveCard(player,card);
        CommandManager commandManager = new CommandManager();
        commandManager.executeCommand(command);
        assertTrue(player.getHand().contains(card));
    }

    @Test
    public void TakeCard() throws Command.IllegalExecutionException {
        Player player = new Player("ev");
        Territory territory = new Territory("Mexico");
        RiskCard card = new RiskCard(territory, RiskCard.RISK_CARD_TYPE.CAVALRY);

        // giving player a card
        Command command = new Player.GiveCard(player,card);
        CommandManager commandManager = new CommandManager();
        commandManager.executeCommand(command);

        //taking player's card... away (:
        Command command2 = new Player.TakeCard(player,card);
        commandManager.executeCommand(command2);
        assertTrue(!player.getHand().contains(card));
    }

    @Test
    public void GiveCardUndo() throws Command.IllegalExecutionException, Command.IllegalUndoException {
        Player player = new Player("Avocado");
        Territory territory = new Territory("Mexico");
        RiskCard card = new RiskCard(territory, RiskCard.RISK_CARD_TYPE.CAVALRY);
        Command command = new Player.GiveCard(player,card);
        CommandManager commandManager = new CommandManager();
        commandManager.executeCommand(command);
        assertTrue(player.getHand().contains(card));
        commandManager.undo();
        assertTrue(!player.getHand().contains(card));
    }

    @Test
    public void TakeCardUndo() throws Command.IllegalExecutionException, Command.IllegalUndoException {
        Player player = new Player("ev");
        Territory territory = new Territory("Russia");
        RiskCard card = new RiskCard(territory, RiskCard.RISK_CARD_TYPE.CAVALRY);

        // giving player a card
        Command command = new Player.GiveCard(player,card);
        CommandManager commandManager = new CommandManager();
        commandManager.executeCommand(command);

        //taking player's card... away (:
        Command command2 = new Player.TakeCard(player,card);
        commandManager.executeCommand(command2);
        assertTrue(!player.getHand().contains(card));

        //give player card again
        commandManager.undo();
        assertTrue(player.getHand().contains(card));
    }

    @Test
    public void TransferCredit() throws Command.IllegalExecutionException {
        Player playerGivingCredit = new Player("PAC",99);
        int playerGivingCreditNum = playerGivingCredit.getCreditTotal();
        Player playerReceivingCredit = new Player("Ted Cruz",0);
        int playerReceivingCreditNUm = playerReceivingCredit.getCreditTotal();
        ArrayList<Player> playerArrayList = new ArrayList<>();
        playerArrayList.add(playerGivingCredit);
        playerArrayList.add(playerReceivingCredit);
        UI ui = new TestUI(playerArrayList);

        Command command = new Player.TransferCredit(playerGivingCredit,playerReceivingCredit,90,ui);
        CommandManager commandManager = new CommandManager();
        commandManager.executeCommand(command);
        assertTrue(playerReceivingCredit.getCreditTotal() == 90);
        assertTrue(playerGivingCredit.getCreditTotal() == 9);
    }

    @Test
    public void userBuyingCard_NotEnoughCredit() {
        boolean flag = false;
        Player playerBuyingCard = new Player("Someone", 0);
        List<Player> playerArrayList = new ArrayList<>();
        playerArrayList.add(playerBuyingCard);
        UI ui = new TestUI(playerArrayList);
        SingleUIGame game = new SingleUIGame();
        game.addUi(ui);
        // this is just for testing, in the actual game, the risk card stack will be
        // automatically created based on number of territories
        game.riskCardStack.push((new RiskCard(RiskCard.RISK_CARD_TYPE.CAVALRY)));
        assertThrows(NotEnoughCreditException.class, () -> {
            game.buyRiskCards(playerBuyingCard);
        });
    }

    @Test
    public void userBuyingCard_HasEnoughCredit() {
        Player playerBuyingCard = new Player("Someone", 20);
        List<Player> playerArrayList = new ArrayList<>(Arrays.asList(playerBuyingCard));
        UI ui = new TestUI(playerArrayList);
        SingleUIGame game = new SingleUIGame();
        // this is just for testing, in the actual game, the risk card stack will be
        // automatically created based on number of territories
        game.riskCardStack.push((new RiskCard(RiskCard.RISK_CARD_TYPE.CAVALRY)));
        try {
            game.buyRiskCards(playerBuyingCard);
        } catch (NotEnoughCreditException e) {
            e.printStackTrace();
        } catch (CreditCardPrompt creditCardPrompt) {
            creditCardPrompt.printStackTrace();
        }

        assertTrue(playerBuyingCard.getHand().get(0).getType().equals(RiskCard.RISK_CARD_TYPE.CAVALRY));
    }

    @Test
    public void playerBuyingUndoAction_HasEnoughCredit(){
        Player playerBuyingUndo = new Player("Jack",4);
        ArrayList<Player> playerArrayList = new ArrayList<>();
        playerArrayList.add(playerBuyingUndo);
        UI ui = new TestUI(playerArrayList);
        SingleUIGame game = new SingleUIGame();
        Territory test = new Territory("Test");
        try {
            game.getCommandManager().executeCommand(new Territory.DraftOneArmy(test));
        } catch (Command.IllegalExecutionException e) {
            e.printStackTrace();
        }
        assertEquals(1, test.getArmies());
        try {
            game.buyUndo(playerBuyingUndo);
        } catch (NotEnoughCreditException e) {
            e.printStackTrace();
        }
        assertEquals(0, test.getArmies());
    }






}
