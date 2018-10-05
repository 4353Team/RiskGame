package riskgame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import riskgame.commands.Command;
import riskgame.commands.CommandManager;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.Player;
import riskgame.ui.UI;

import java.util.ArrayList;
import java.util.List;

public class SingleUIGame implements GameEngine {
    private static final Logger logger = LogManager.getLogger(SingleUIGame.class);
    private final CommandManager commandManager = new CommandManager();
    private UI ui;
    private List<Territory> territories;
    private GameState gameState;
    private Player currentPlayer;
    private List<Player> playerOrderList = new ArrayList<>();

    public SingleUIGame() {
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
            try {
                switch (gameState) {
                    case SELECT_MAP:
                        GameMaps.GameMap selectedGameMap = ui.selectMap(GameMaps.instanceOf());
                        Command selectMap = new SelectMap(this, selectedGameMap);
                        commandManager.executeCommand(selectMap);
                        break;
                    case SELECT_PLAYERS:
                        List<Player> list = ui.selectAndNamePlayers();
                        Command setPlayers = new SetPlayers(this, list);
                        commandManager.executeCommand(setPlayers);
                        break;
                    case INIT_DRAFT:
                        // not finished - this is a test essentially
                        int armiesDrafted = 0;
                        while (armiesDrafted < 50) {
                            Territory territory = ui.getInitDraftPick(currentPlayer);
                            Command draftOneInit = new DraftOneInit(this, territory, currentPlayer);
                            commandManager.executeCommand(draftOneInit); // selects the next player in the command as
                            // well
                            armiesDrafted++;
                        }
                        gameState = GameState.END;
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

    @Override
    public void undo() throws Exception {
        commandManager.undo();
    }

    @Override
    public void redo() throws Exception {
        commandManager.redo();
    }

    @Override
    public List<Territory> getMap() {
        return territories;
    }

    private void nextPlayer() {
        currentPlayer = playerOrderList.get(
                (playerOrderList.indexOf(currentPlayer) + 1) % playerOrderList.size());
    }

    private void previousPlayer() {
        currentPlayer = playerOrderList.get(
                (playerOrderList.indexOf(currentPlayer) - 1) % playerOrderList.size());
    }

    private enum GameState {SELECT_MAP, SELECT_PLAYERS, INIT_DRAFT, DRAFT, ATTACK, FORTIFY, END}

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
            game.nextPlayer();
            game.ui.update();
        }

        @Override
        public void undo() throws IllegalUndoException {
            draftOne.undo();
            territory.setControlledBy(previousOwner);
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
}
