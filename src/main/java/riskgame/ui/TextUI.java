package riskgame.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import riskgame.GameEngine;
import riskgame.GameMaps;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.CreditCardPrompt;
import riskgame.gameobject.player.HumanPlayer;
import riskgame.gameobject.player.NotEnoughCreditException;
import riskgame.gameobject.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TextUI implements UI {
    private final PrintStream outStream;
    private final Scanner reader;
    private GameEngine gameEngine;
    public List<Territory> map;
    private List<Player> playerSetup;
    private static final Logger logger = LogManager.getLogger(TextUI.class);

    public TextUI(PrintStream writer, Scanner reader) {
        this.outStream = writer;
        this.reader = reader;
    }

    @Override
    public void addGame(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }


    @Override
    public void update() {
        map = gameEngine.getMap();
    }

    @Override
    public GameMaps.GameMap selectMap(GameMaps gameMaps) {
        Integer chosenMap = null;
        List<GameMaps.GameMap> mapsList = gameMaps.getList();

        do {
            outStream.println("Please select a map number from the list: ");

            for (int i = 0; i < mapsList.size(); i++) {
                outStream.println("[" + mapsList.get(i).getName() + "]: " + (i + 1));
            }
            chosenMap = getNextInt()-1;

        } while (!(chosenMap >= 0 && chosenMap < mapsList.size()));

        return mapsList.get(chosenMap);
    }

    private Integer getNextInt() {
        Integer selection = null;
        try {
            selection = reader.nextInt();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return selection;
    }

    @Override
    public void error(Exception e) {
        outStream.println(e.getMessage());
    }

    @Override
    public List<Player> selectAndNamePlayers() {
        List<Player> playerList = new ArrayList<>();
        outStream.println("How many players? ");

        Integer numberOfPlayers = getNextInt();

        for (int i = 0; i < numberOfPlayers; i++) {
            outStream.println("Player " + i + " enter your name: ");
            playerList.add(new HumanPlayer(reader.next()));
            logger.info(playerList.get(i).getName() + " has been added to the player list.");
        }

        outStream.println("Rolling dice...\n" +
                "Player turn order:");
        Collections.shuffle(playerList);
        for (int i = 0; i < playerList.size(); i++) {
            outStream.println((i + 1) + ". " + playerList.get(i).getName());
        }
        return playerList;
    }


    @Override
    public Territory getInitDraftPick(Player currentPlayer, List<Territory> territories) {
        outStream.println(currentPlayer.getName() + ", select a territory to occupy");
        displayTerritories(territories);
        Integer pickedTerritory = getNextInt();
        outStream.println(currentPlayer.getName() + " claimed " + getTerritory(pickedTerritory, territories));
        return getTerritory(pickedTerritory, territories);
}

    private Territory getTerritory(Integer territoryNumber, List<Territory> territories) {
        return territories.get(territoryNumber - 1);
    }

    private void displayTerritories(List<Territory> territories) {
        outStream.println("Territory Summary: ");
        for (int i = 0; i < territories.size(); i++) {
            outStream.println("["+territories.get(i).getName()+"]: " +
                    (i+1) + " --> " + territories.get(i).getControlledBy().getName() +
                    " (" + territories.get(i).getArmies() + " armies)");
        }
    }

    @Override
    public int creditCardPrompt(CreditCardPrompt creditCardPrompt) throws CreditPromptCancelledException {
        return 0;
    }

    @Override
    public void notEnoughCredit(NotEnoughCreditException e) {

    }

    @Override
    public Territory.AttackPick getAttackPick(Player currentPlayer) {
        return null;
    }

    @Override
    public int queryArmiesToMove(Player currentPlayer, Territory from, Territory to) {
        return 0;
    }

    @Override
    public void tellPlayersToClaimTheirFirstTerritories() {
        outStream.println("Time to claim your first territories!");
    }
}
