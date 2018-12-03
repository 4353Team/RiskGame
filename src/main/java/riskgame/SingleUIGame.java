package riskgame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import riskgame.commands.Command;
import riskgame.commands.CommandManager;
import riskgame.gameobject.RiskCard;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.CreditCardPrompt;
import riskgame.gameobject.player.NotEnoughCreditException;
import riskgame.gameobject.player.Player;
import riskgame.gameobject.player.PlayerCredit;
import riskgame.ui.UI;

import java.util.*;

public class SingleUIGame implements GameEngine {
    private static final Logger logger = LogManager.getLogger(SingleUIGame.class);
    public static final int UNDO_PRICE = 1;
    public static final int RISK_CARD_PRICE = 1;

    private final CommandManager commandManager = new CommandManager();
    private UI ui;
    private List<Territory> territories;
    GameState gameState;
    private Player currentPlayer;
    private List<Player> playerOrderList = new ArrayList<>();
    protected Stack<RiskCard> riskCardStack = new Stack<>();
    private Integer armies = 0;
    private Integer armiesDrafted = 0;

    private Territory lastAttacking;
    private Territory lastDefending;

    public SingleUIGame() {
    }

    // might add this to GameEngine interface
    public void createCardStack() {
        //risk card types
        List<RiskCard.RISK_CARD_TYPE> riskCardTypes = new ArrayList<>(
                Arrays.asList(RiskCard.RISK_CARD_TYPE.ARTILLERY,
                        RiskCard.RISK_CARD_TYPE.CAVALRY,
                        RiskCard.RISK_CARD_TYPE.INFANTRY));
        for (int i = 0; i < territories.size(); i++) {
            riskCardStack.push(new RiskCard(territories.get(i), riskCardTypes.get(i % 3)));
        }
    }

    // might add this to GameEngine interface
    public void setNumArmiesForGame() {
        //If 3 are playing, each player counts out 35 Infantry.
        //If 4 are playing, each player counts out 30 Infantry.
        //If 5 are playing, each player counts out 25 Infantry.
        //If 6 are playing, each player counts out 20 Infantry.
        int numberOfPlayers = playerOrderList.size();
        if (numberOfPlayers == 3)
            armies = 35;
        else if (numberOfPlayers == 4)
            armies = 30;
        else if (numberOfPlayers == 5)
            armies = 25;
        else if (numberOfPlayers == 6)
            armies = 20;
    }

    @Override
    public void addUi(UI ui) {
        this.ui = ui;
    }

    @Override
    public void start() throws Exception {

        gameState = GameState.SELECT_MAP;

        boolean exit = false;
        while (!exit) {
            try { // every GameState sets the next state
                switch (gameState) {
                    // GameState.SELECT_MAP
                    case SELECT_MAP:
                        // UI's input
                        GameMaps.GameMap selectedGameMap = ui.selectMap(GameMaps.instanceOf()); // give the UI data to display possible maps
                        // Use UI's input for new Command
                        Command selectMap = new SelectMap(this, selectedGameMap);
                        // Execute the Command
                        commandManager.executeCommand(selectMap);
                        createCardStack();
                        break;
                    case SELECT_PLAYERS:
                        List<Player> list = ui.selectAndNamePlayers();
                        Command setPlayers = new SetPlayers(this, list);
                        commandManager.executeCommand(setPlayers);
                        setNumArmiesForGame();
                        break;
                    case INIT_DRAFT:
                        ui.tellPlayersToClaimTheirFirstTerritories();

                        while (armiesDrafted < territories.size()) {
                            Territory pickedTerritory = ui.getInitDraftPick(currentPlayer, territories);
                            while (!(pickedTerritory.getControlledBy() == Territory.NoOwner || pickedTerritory.getControlledBy() == currentPlayer)) {
                                ui.error(new Exception("Pick a territory that is unoccupied or belongs to you."));
                                pickedTerritory = ui.getInitDraftPick(currentPlayer, territories);
                            }
                            Command draftOneInit = new DraftOneInit(this, pickedTerritory, currentPlayer);
                            commandManager.executeCommand(draftOneInit); // selects the next player in the command as well
                            armiesDrafted++;
                        }
                        //After all territories are claimed,
                        // each player in turn places one additional army onto any territory he or she already occupies.
                        while(armiesDrafted < armies){
                            Territory pickedTerritory = ui.getInitDraftPick(currentPlayer, territories);
                            while(!(pickedTerritory.getControlledBy() == currentPlayer)){
                                ui.error(new Exception("Pick a territory that belongs to you."));
                                pickedTerritory = ui.getInitDraftPick(currentPlayer, territories);
                            }
                            Command draftOneInit = new DraftOneInit(this, pickedTerritory, currentPlayer);
                            commandManager.executeCommand(draftOneInit); // selects the next player in the command as well
                            armiesDrafted++;
                        }
                        currentPlayer = playerOrderList.get(0); //Whoever placed the first army opens the game.
                        undoableNextPhase(GameState.DRAFT);
                        break;
                    //Getting and placing new armies.
                    case DRAFT:
                        System.out.println("\n»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»» DRAFT PHASE ««««««««««««««««««««««««««««««««««««««««««««");
                        /*
                        check if player receives any more armies based on a number of factors:
                        */
                        int armiesToReceive = 0;

                        //The number of territories player occupies.
                        int territoriesOwned = currentPlayer.getNumTerritories();
                        armiesToReceive = armiesToReceive + (territoriesOwned/3);

                        //The value of the continents player controls.


                        //The value of the matched sets of RISK cards you trade in.
                        //The specific territory pictured on a traded-in card.

                        //receive armies
                        currentPlayer.addArmies(armiesToReceive);

                        //deploy armies
                        String response = "";
                        while(!response.equals("N") && currentPlayer.getArmies() > 0){
                            response = ui.askPlayerIfToDraft(currentPlayer);
                            Map.Entry<Territory, Integer> draftChoicePair = ui.getDraftPick(currentPlayer, territories);
                            Territory pickedTerritory = draftChoicePair.getKey();
                            Integer armiesToDraft = draftChoicePair.getValue();
                            Command draft = new Draft(this, pickedTerritory, currentPlayer, armiesToDraft);
                            commandManager.executeCommand(draft); // selects the next player in the command as well
                        }
                        undoableNextPhase(GameState.ATTACK);
                        break;
                    case ATTACK:
                        System.out.println("\n»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»» ATTACK PHASE ««««««««««««««««««««««««««««««««««««««««««««");
                        try {
                            ui.askPlayerIfToAttack(currentPlayer);
                            Territory.AttackPick attackPick = ui.getAttackPick(currentPlayer, territories);
                            try {
                                attackPick.checksOut(currentPlayer); // check that everything is good, if not it will loop around

                                Command attack = new Territory.Attack(attackPick.attackingTerritory, attackPick.defendingTerritory, this);
                                commandManager.executeCommand(attack);
                                lastAttacking = attackPick.attackingTerritory;
                                lastDefending = attackPick.defendingTerritory;

                            } catch (Territory.AttackPick.AttackPickException exception) {
                                ui.error(exception); // will try again
                            }
                        } catch (UI.NoMoreAttackException e) {
                            commandManager.executeCommand(new FortifyPhase(this));
                        } catch (UI.EndGameException e) {
                            undoableNextPhase(GameState.END);
                        }
                        break;

                    case ATTACK_SUCCESSFUL:
                        int armiesToMoveIn = ui.queryArmiesToMove(currentPlayer, lastAttacking, lastDefending);
                        if (lastAttacking.getArmies() - armiesToMoveIn > 1) { // todo: research if 1 army left in a territory is allowed or is it 2
                            Command command = new Territory.MoveArmies(armiesToMoveIn, lastAttacking, lastDefending);
                            commandManager.executeCommand(command);
                            undoableNextPhase(GameState.ATTACK);
                        } else {
                            ui.error(new Exception("You are not allowed to move that many armies, you must leave 1 army"));
                        }
                        break;

                    case FORTIFY:
                        undoableNextPhase(GameState.END);
                        FortifyPick fortifyPick = ui.getFortifyPick(this.currentPlayer);
                        while (!verifyFortifyPick(fortifyPick)) {
                            ui.error(new Exception("Fortify pick invalid"));
                            fortifyPick = ui.getFortifyPick(this.currentPlayer);
                        }
                        commandManager.executeCommand(new FortifyCommand(fortifyPick));
                        commandManager.executeCommand(new NextPlayerCommand(this));
                        undoableNextPhase(GameState.DRAFT);
                        break;


                    case END:
                        exit = true;
                        break;
                }
            } catch (Exception e) {
                ui.error(e);
                throw e;
            }
        }
    }

    private boolean verifyFortifyPick(FortifyPick fortifyPick) {
        if (fortifyPick.from.getControlledBy() != currentPlayer) return false;
        if (fortifyPick.to.getControlledBy() != currentPlayer) return false;
        if (fortifyPick.from.getArmies() - 1 < fortifyPick.howManyArmies) return false;
        return true;
    }

    /**
     * eventually remove this and replace with just buyUndo()
     *
     * @throws Exception
     */
    @Override
    public void undo() throws Exception {
        commandManager.undo();
    }

    @Override
    public void redo() throws Exception {
        commandManager.redo();
    }

    @Override
    public void buyUndo(Player player) throws NotEnoughCreditException {
        try {
            player.getCredit().removeCredit(UNDO_PRICE);
            commandManager.undo();
        } catch (CreditCardPrompt creditCardPrompt) {
            try {
                int creditToAdd = ui.creditCardPrompt(creditCardPrompt);
                creditCardPrompt.credit.addCredit(creditToAdd);

            } catch (UI.CreditPromptCancelledException ignored) {
            }

        } catch (Command.IllegalUndoException ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public void buyRiskCards(Player buyer) throws NotEnoughCreditException, CreditCardPrompt {
        PlayerCredit creditToUse = buyer.getCredit();
        try {
            creditToUse.removeCredit(RISK_CARD_PRICE);

            Command command = new TakeTopOfStackAndGiveToPlayer(this, buyer);
            commandManager.executeCommand(command);
//this should be handled when/where buyRiskCards is called
//        } catch (NotEnoughCreditException e) {
//            ui.notEnoughCredit(e);
        } catch (CreditCardPrompt creditCardPrompt) { // credit card prompt has information on credit
            try {
                int creditToAdd = ui.creditCardPrompt(creditCardPrompt); // you ain't getting yo money back :P
                creditCardPrompt.credit.addCredit(creditToAdd);

            } catch (UI.CreditPromptCancelledException ignored) {
            }
        } catch (Command.IllegalExecutionException ignored) {
        }
    }

    @Override
    public List<Territory> getMap() {
        return territories;
    }

    @Override
    public void disableCommandLogs() {
        commandManager.disableLogs();
    }

    private void nextPlayer() {
        currentPlayer = playerOrderList.get(
                (playerOrderList.indexOf(currentPlayer) + 1) % playerOrderList.size());
    }

    private void previousPlayer() {
        currentPlayer = playerOrderList.get(
                (playerOrderList.indexOf(currentPlayer) - 1) % playerOrderList.size());
    }

    public void attackWon() {
        assert gameState == GameState.ATTACK;
        gameState = GameState.ATTACK_SUCCESSFUL;
    }

    public void attackWonUndo() {
        assert gameState == GameState.ATTACK_SUCCESSFUL;
        gameState = GameState.ATTACK;
    }

    CommandManager getCommandManager() {
        return commandManager;
    }

    enum GameState {SELECT_MAP, SELECT_PLAYERS, INIT_DRAFT, DRAFT, ATTACK, ATTACK_SUCCESSFUL, FORTIFY, END}

    private class SelectMap implements Command {
        private final SingleUIGame gameEngine;
        private final GameMaps.GameMap selectedGameMap;
        private final GameState before;

        public SelectMap(SingleUIGame gameEngine, GameMaps.GameMap selectedGameMap) {
            this.gameEngine = gameEngine;
            this.selectedGameMap = selectedGameMap;
            before = gameEngine.gameState;
        }

        /**
         * Logs the action of the command
         */
        @Override
        public void log() {
            logger.info("The game map has been selected!" + System.lineSeparator() +
                    "The name of the chosen game map is: " + selectedGameMap.getName());
        }

        @Override
        public void execute() throws IllegalExecutionException {
            gameEngine.territories = selectedGameMap.getConfiguredTerritories();
            gameEngine.gameState = GameState.SELECT_PLAYERS;
            gameEngine.ui.update();
            assert before != gameEngine.gameState;
        }

        @Override
        public void undo() throws IllegalUndoException {
            gameEngine.gameState = before;
            gameEngine.territories = null;
        }
    }

    private void undoableNextPhase(GameState nextGameState) {
        Command nextPhase = new NextPhase(this, nextGameState);
        try {
            commandManager.executeCommand(nextPhase);
        } catch (Command.IllegalExecutionException e) {
            e.printStackTrace();
        }
    }

    private class SetPlayers implements Command {

        private final SingleUIGame game;
        private final List<Player> list;
        private final GameState before;

        public SetPlayers(SingleUIGame game, List<Player> list) {
            this.game = game;
            this.list = list;
            before = game.gameState;
            assert before == GameState.SELECT_PLAYERS;
        }

        /**
         * Logs the action of the command
         *
         * @assumed there will be at least one Player
         */
        @Override
        public void log() {
            StringBuilder toLog = new StringBuilder("The Players have been decided!" + System.lineSeparator() +
                    "The first Player to go will be: " + list.get(0).getName());
            for (int i = 1; i < list.size(); i++) {
                toLog.append(System.lineSeparator() + "Then, Player: " + list.get(i).getName());
            }
            logger.info(toLog);
        }

        @Override
        public void execute() throws IllegalExecutionException {
            game.playerOrderList = list;
            game.currentPlayer = list.get(0);
            game.gameState = GameState.INIT_DRAFT;
            game.ui.update();
        }

        @Override
        public void undo() throws IllegalUndoException {
            game.playerOrderList = null;
            game.currentPlayer = null;
            game.gameState = before;
            game.ui.update();
        }
    }

    private class Draft implements Command{
        private final Territory territory;
        private final Player player;
        private final Command draftOne;
        private SingleUIGame game;
        private final Integer armies;

        public Draft(SingleUIGame game, Territory territory, Player player, Integer armies){
            this.game = game;
            this.territory = territory;
            this.player = player;
            this.armies = armies;
            draftOne = new Territory.DraftOneArmy(territory);
        }
        @Override
        public void log() {
            logger.info("Player: " + player.getName() + " is drafting " + armies + " armies to Territory: " + territory.getName());
        }

        @Override
        public void execute() throws IllegalExecutionException {
            for (int i = 0; i < armies; i++) {
                draftOne.execute();
            }
            player.removeArmies(armies);
            game.gameState = GameState.DRAFT;
            game.ui.update();
        }

        @Override
        public void undo() throws IllegalUndoException {
            for (int i = 0; i < armies; i++) {
                draftOne.undo();
            }
            player.addArmies(armies);
            game.ui.update();
        }
    }

    private class DraftOneInit implements Command {
        private final Territory territory;
        private final Player player;
        private final Command draftOne;
        private final Player previousOwner;
        private SingleUIGame game;

        public DraftOneInit(SingleUIGame game, Territory territory, Player player) {
            this.game = game;
            this.territory = territory;
            this.player = player;
            previousOwner = territory.getControlledBy();
            draftOne = new Territory.DraftOneArmy(territory);
        }

        /**
         * Logs the action of the command
         */
        @Override
        public void log() {
            logger.info("Player: " + player.getName() + " is drafting one army to Territory: " + territory.getName());
        }

        @Override
        public void execute() throws IllegalExecutionException {
            if (previousOwner != Territory.NoOwner && previousOwner != player)
                throw new IllegalExecutionException(new OtherPlayerControlsTerritoryException(player, territory));
            draftOne.execute();
            territory.setControlledBy(player);
            player.addTerritory(territory);
            game.nextPlayer();
            game.ui.update();
        }

        @Override
        public void undo() throws IllegalUndoException {
            draftOne.undo();
            territory.setControlledBy(previousOwner);
            player.removeTerritory(territory);
            game.previousPlayer();
            game.ui.update();
        }
    }

    private class OtherPlayerControlsTerritoryException extends Throwable {
        public final Player player;
        public final Territory territory;

        public OtherPlayerControlsTerritoryException(Player player, Territory territory) {
            super("There was an attempt by " + player.getName() + " to draft to " +
                    territory.getName() + ", which is already controlled by " + territory.getControlledBy().getName() + ".");
            this.player = player;
            this.territory = territory;
        }
    }


    private class TakeTopOfStackAndGiveToPlayer implements Command {
        private final SingleUIGame singleUIGame;
        private final Player player;
        private RiskCard givenCard;

        Command giveCardCommand;

        public TakeTopOfStackAndGiveToPlayer(SingleUIGame singleUIGame, Player player) {
            this.singleUIGame = singleUIGame;
            this.player = player;
        }

        @Override
        public void log() {

        }

        @Override
        public void execute() throws IllegalExecutionException {
            givenCard = singleUIGame.riskCardStack.pop();

            giveCardCommand = new Player.GiveCard(player, givenCard);

            giveCardCommand.execute();
        }

        @Override
        public void undo() throws IllegalUndoException {
            singleUIGame.riskCardStack.push(givenCard);

            giveCardCommand.undo();
        }
    }

    private class FortifyPhase implements Command {
        private SingleUIGame game;
        private GameState beforeGameState;


        public FortifyPhase(SingleUIGame game) {
            this.game = game;
            beforeGameState = GameState.valueOf(game.gameState.name());
        }

        @Override
        public void log() {
            logger.info("Moving to fortify phase");
        }

        @Override
        public void execute() throws IllegalExecutionException {
            game.gameState = GameState.FORTIFY;
        }

        @Override
        public void undo() throws IllegalUndoException {
            game.gameState = GameState.valueOf(beforeGameState.name());
        }
    }

    public static class FortifyPick {
        public final Territory from;
        public final Territory to;
        public final int howManyArmies;

        public FortifyPick(Territory from, Territory to, int howManyArmies) {
            this.from = from;
            this.to = to;
            this.howManyArmies = howManyArmies;
        }
    }

    private class NextPlayerCommand implements Command {
        private final SingleUIGame game;
        private final Player initalPlayer;

        public NextPlayerCommand(SingleUIGame game) {
            this.game = game;
            initalPlayer = game.currentPlayer;
        }

        @Override
        public void log() {
            logger.info("The next player is up...");
        }

        @Override
        public void execute() throws IllegalExecutionException {
            game.nextPlayer();
        }

        @Override
        public void undo() throws IllegalUndoException {
            game.previousPlayer();
        }
    }

    // note: does not check if it's valid
    private class FortifyCommand implements Command {
        private FortifyPick fortifyPick;

        public FortifyCommand(FortifyPick fortifyPick) {
            this.fortifyPick = fortifyPick;
        }

        @Override
        public void log() {
            logger.info("Moving " + fortifyPick.howManyArmies + " armies from " +
                    fortifyPick.from.getName() + " to " + fortifyPick.to.getName());
        }

        @Override
        public void execute() throws IllegalExecutionException {
            fortifyPick.from.removeArmies(fortifyPick.howManyArmies);
            fortifyPick.to.addArmies(fortifyPick.howManyArmies);
        }

        @Override
        public void undo() throws IllegalUndoException {
            fortifyPick.from.addArmies(fortifyPick.howManyArmies);
            fortifyPick.to.removeArmies(fortifyPick.howManyArmies);
        }
    }

    private class NextPhase implements Command {
        final GameState previousState;
        final SingleUIGame game;
        final GameState nextState;

        public NextPhase(SingleUIGame game, GameState nextGameState) {
            this.game = game;
            nextState = nextGameState;
            previousState = game.gameState;
        }

        @Override
        public void log() {
            logger.info("Game state moving from: " + previousState.name() + " to " + nextState.name());
        }

        @Override
        public void execute() throws IllegalExecutionException {
            game.gameState = nextState;
        }

        @Override
        public void undo() throws IllegalUndoException {
            game.gameState = previousState;
        }
    }
}
