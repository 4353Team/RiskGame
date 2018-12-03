package riskgame.ui;

import javafx.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import riskgame.GameEngine;
import riskgame.GameMaps;
import riskgame.SingleUIGame;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.CreditCardPrompt;
import riskgame.gameobject.player.HumanPlayer;
import riskgame.gameobject.player.NotEnoughCreditException;
import riskgame.gameobject.player.Player;

import java.io.*;
import java.util.*;

public class TextUI implements UI {
    private final PrintStream outStream;
    private final BufferedReader reader;
    private GameEngine gameEngine;
    private List<Territory> map;
    private List<Player> playerSetup;
    private static final Logger logger = LogManager.getLogger(TextUI.class);
    private Player currentPlayer;

    public TextUI(PrintStream writer, BufferedReader reader) {
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
            String readLine = reader.readLine();
            readLine = readLine.replaceAll(" ","");
            selection = Integer.parseInt(readLine);
        } catch (NumberFormatException e) {
            logger.error("not able to read number: " + e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("***Entered: " + selection);
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
            playerList.add(new HumanPlayer(getNextString()));
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

    private String getNextString() {
        try {
            String readLine = reader.readLine();
            System.out.println("***Entered: " + readLine);
            return readLine;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // todo, handle this better
    }


    @Override
    public Territory getInitDraftPick(Player currentPlayer, List<Territory> territories) {
        outStream.println(currentPlayer.getName() + ", select a territory to occupy");
        displayTerritories(territories);
        Integer pickedTerritory = getNextInt();
        outStream.println(currentPlayer.getName() + " claimed " + getTerritory(pickedTerritory, territories));
        return getTerritory(pickedTerritory, territories);
}

    public Pair getDraftPick(Player currentPlayer, List<Territory>territories){
        outStream.println(currentPlayer.getName() + ", select a territory to draft to");
        displayTerritories(territories);
        Integer pickedTerritoryNum = getNextInt();

        Territory pickedTerritory = getTerritory(pickedTerritoryNum,territories);
        while(!(pickedTerritory.getControlledBy() == currentPlayer)){
            error(new Exception("Pick a territory that belongs to you."));
            outStream.println(currentPlayer.getName() + ", select a territory to draft to");
            displayTerritories(territories);
            pickedTerritoryNum = getNextInt();
            outStream.println(currentPlayer.getName() + ", selected " + pickedTerritory.getName());
        }

        displayPlayerArmies(currentPlayer);
        outStream.println(currentPlayer.getName() + ", select number of armies to draft");
        Integer armiesToDraft = getNextInt();
        outStream.println(currentPlayer.getName() + " drafted " + armiesToDraft + " armies to " + pickedTerritory);
        Pair<Territory, Integer> draftMapping = new Pair<Territory, Integer>(getTerritory(pickedTerritoryNum,territories), armiesToDraft);
        return draftMapping;
    }

    private Territory getTerritory(Integer territoryNumber, List<Territory> territories) {
        return territories.get(territoryNumber - 1);
    }

    private void displayTerritories(List<Territory> territories) {
        outStream.println("+-------------+-------------------+\n" +
                "|             |                   |\n" +
                "|   Alberta   |     Ontario       |\n" +
                "|      2      |          1        |\n" +
                "++------------+-------+-----------+-------+\n" +
                " |                    |                   |\n" +
                " |    Western         |     Eastern       |\n" +
                " |   United States    |    United States  |\n" +
                " |                    |                   |\n" +
                " |        3           |         4         |\n" +
                " +-----------------+--+---+---------------+\n" +
                "                   | C.A  |\n" +
                "                   |      |\n" +
                "                   |  5   |\n" +
                "                   +--+---+----------+\n" +
                "                      |  Venezuela   |\n" +
                "                      |      6       |\n" +
                "               +------+----+---------+------+\n" +
                "               |           |                |\n" +
                "               |   Peru    |     Brazil     |\n" +
                "               |     7     |       8        |\n" +
                "               +---------+-+---+------------+\n" +
                "                         |     |\n" +
                "                         | Arg |\n" +
                "                         |  9  |\n" +
                "                         +-----+\n");
        outStream.println("Territory Summary: ");
        for (int i = 0; i < territories.size(); i++) {
            outStream.println("["+territories.get(i).getName()+"]: " +
                    (i+1) + " --> " + territories.get(i).getControlledBy().getName() +
                    " (" + territories.get(i).getArmies() + " armies)");
        }
    }

    private void displayPlayerArmies(Player player){
        outStream.println(player.getName() + " has " + player.getArmies() + " armies.");
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

    @Override
    public String askPlayerIfToDraft(Player currentPlayer){
        outStream.println(currentPlayer.getName() + ", do you want to draft more armies? (Y/N)");
        String response = getNextString();
        return response;
    }

    @Override
    public String askPlayerIfToAttack(Player currentPlayer){
        outStream.println(currentPlayer.getName() + ", do you want to attack? (Y/N");
        String response = getNextString();
        return response;
    }

    @Override
    public SingleUIGame.FortifyPick getFortifyPick(Player currentPlayer) {
        return null;
    }
}
