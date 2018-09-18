package com.company;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {
    private int maxPlayers = 6;
    private int minPlayers = 2;
    private int initialInfantryCount = 0;
    public Map map;
    private Country territorySelected = null;
    boolean show = false;
    public List<Player> players;
    public Dice gameDice;
    private Stack<Card> cardStack;
    public List<Card> cards;
   // private int turn = 0;
    private static int totalTurnsCounter = 0;
    private int phase = 0;
    private int numberOfPlayers = 0;
    private int setsOfRiskCardsTraded = 0;
    Country attackingCountry;
    Country defendingCountry;
    Scanner scan = new Scanner(System.in);
    boolean nowAttacking = false;
    int territoriesCapturedThisTurn;
    private List<Player> clonePlayers;
    InfoBox attackBox = new InfoBox();
    InfoBox draftBox = new InfoBox();
    InfoBox fortifyBox = new InfoBox();
    InfoBox nextBox = new InfoBox();
    NumberSelector numSelector = new NumberSelector();
    Turn turn;
    List<InfoBox> controlBoxes = new ArrayList<>();
    private boolean attackSelected = false;
    private boolean defenseSelected = false;



    // initialize map
    public Game() {
        controlBoxes.add(attackBox);
        controlBoxes.add(draftBox);
        controlBoxes.add(fortifyBox);
        controlBoxes.add(nextBox);
        numSelector.setCoordinate(new Location(250,720));
        numSelector.setDimension(new Dimension(30,30));
        numSelector.setLowerBound(1);
        numSelector.setUpperBound(6);
        numSelector.setVisible(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
         //       paintComponents(getGraphics());

                for (InfoBox b :
                        controlBoxes) {
                    if (b.contains(e.getPoint())){
                        b.setBackgrounColor(turn.getTurnPlayer().getPlayerColor());
                    }

                }
                if(nextBox.contains(e.getPoint())){
              //      turn.setTurnPlayer(turn.nextTurn(turn.getTurnPlayer()));
                    turn.nextGamePhase();
                 //   System.out.println(turn.getTurnPlayer().getName());
//                   / System.exit(0);
                }


                for (Country c : map.countries) {
                    if (c.contains(e.getPoint())) {

                        if (totalTurnsCounter <= 42) {
                            if (c.getOwnedBy() == null) {
                                c.addOneInfantry(turn.getTurnPlayer());
                                turn.getTurnPlayer().addTerritory(c);
                                turn.setTurnPlayer(turn.nextTurn(turn.getTurnPlayer()));
                                System.out.println(totalTurnsCounter);
                                System.out.println(cards.get(0).getTerritory().getName());


                            }
                        }
                        System.out.println(turn.getTurnPlayer().getTotalInitialTroops());
                        if (turn.getTurnPlayer().getTerritories().contains(c) && turn.getTurnPlayer().getTotalInitialTroops() < 0 && totalTurnsCounter >= 42) {
                            c.addOneInfantry(turn.getTurnPlayer());
                            turn.getTurnPlayer().addTerritory(c);

                            turn.setTurnPlayer(turn.nextTurn(turn.getTurnPlayer()));
//                            c.drawSelected(getGraphics(),true);


                        }
                        boolean con = true;
                        for (Player p : players
                        ) {
                            System.out.println(p.getTotalInitialTroops());
                            if (p.getTotalInitialTroops() > 0)
                                con = false;
                        }
                        if (con) {
                            territorySelected = c;
                        }
                        System.out.println(con);
                        boolean defense = false;
                        territorySelected = c;

                        if (turn.getGamePhase() == GamePhase.DRAFT){
                            if (turn.getTurnPlayer().getTerritories().contains(c)) {
                                numSelector.setVisible(true);
                                draftTroops(c,3);

                                turn.nextGamePhase();
                            }

                        } else if (turn.getGamePhase() == GamePhase.ATTACK) {

                                if (!attackSelected) {
                                    if (turn.getTurnPlayer().getTerritories().contains(c) && c.getTroops() > 1) {
                                        c.drawSelected(getGraphics(), GamePhase.ATTACK);
                                        attackingCountry = c;
                                        System.out.println("attacking country: " + attackingCountry.getName());
                                        attackSelected = true;
                                        defenseSelected = false;

                                    }
                                }
                                if (!defenseSelected) {
                                    if (attackingCountry.getNeighbors().contains(c)) {
                                        defendingCountry = c;
                                        System.out.println("defending country: " + defendingCountry.getName());
                                        attack(attackingCountry, defendingCountry);
                                        c.drawSelected(getGraphics(), GamePhase.ATTACK);
                                        attackSelected = true;
                                        defenseSelected = true;

                                    }
                                }



                        }

                    } else {
                        attackSelected = false;
                        defenseSelected = false;
                    }
            }
                     paintComponents(getGraphics());
            }
        });


        setDefaultLookAndFeelDecorated(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setSize(1000,1000);
        setVisible(true);
        setBackground(Color.red);
        System.out.println("rendering");


        System.out.println(("\n" +
        " ██▀███   ██▓  ██████  ██ ▄█▀\n" +
        "▓██ ▒ ██▒▓██▒▒██    ▒  ██▄█▒ \n" +
        "▓██ ░▄█ ▒▒██▒░ ▓██▄   ▓███▄░ \n" +
        "▒██▀▀█▄  ░██░  ▒   ██▒▓██ █▄ \n" +
        "░██▓ ▒██▒░██░▒██████▒▒▒██▒ █▄\n" +
        "░ ▒▓ ░▒▓░░▓  ▒ ▒▓▒ ▒ ░▒ ▒▒ ▓▒\n" +
        "  ░▒ ░ ▒░ ▒ ░░ ░▒  ░ ░░ ░▒ ▒░\n" +
        "  ░░   ░  ▒ ░░  ░  ░  ░ ░░ ░ \n" +
        "   ░      ░        ░  ░  ░   \n" + "\n"));


        List<Color> playerColors = new ArrayList<>();
        playerColors.add(new Color(181 ,45,69));
        playerColors.add(new Color(16,81,135));
        playerColors.add(new Color(24,128,112));
        playerColors.add(new Color(247,147,97));
        playerColors.add(new Color(242,183,5));
        playerColors.add(new Color(79,60,166));

        Collections.shuffle(playerColors);
        //load map
        map = loadMap();

        attackingCountry = null;

        defendingCountry = null;

        // create players for test purpose
        players = new ArrayList<Player>();
        players.add(new Player("Navin"));
        players.add(new Player("Sunada"));
        players.add(new Player("Jack"));
        players.add(new Player("John"));
        players.add(new Player("Mark"));
        players.add(new Player("Tom"));


        turn = new Turn(players);



        for (Player p:players
        ) {
            p.setPlayerColor(playerColors.get(players.indexOf(p)));
            p.setLocation(new Location(20,940-(players.indexOf(p))*35));
        }




        numberOfPlayers = players.size();
        initialInfantryCount = getInitialInfantryCount(numberOfPlayers) * numberOfPlayers;



        for(Player p: players) {
            int a = getInitialInfantryCount(players.size());
            p.setTotalInitialTroops(a);
        }
        System.out.println("We have " + players.size() + " players!");


        turn.setTurnPlayer(players.get(0)); //
        System.out.println(turn.getTurnPlayer().getName()+" going first.");

        ArrayList<Integer> randomCountries = new ArrayList<Integer>();
        for (int i= 0; i < map.countries.size(); i++) {
            randomCountries.add(i);


        }
    //    wait(300);
        Collections.shuffle(randomCountries);

      //  paintComponents(getGraphics());
         for (int i= 0; i < 42; i++) {
            map.countries.get(randomCountries.get(i)).addOneInfantry(turn.getTurnPlayer());
            turn.getTurnPlayer().addTerritory(map.countries.get(randomCountries.get(i)));

            turn.setTurnPlayer(turn.nextTurn(turn.getTurnPlayer()));
        }
   //     paintComponents(getGraphics());


        for (int i = 0; i < initialInfantryCount - 42; i++) {
      //      paintComponents(getGraphics());
            Player curr = turn.getTurnPlayer();
            List<Country> c = getTerritoriesOwnedBy(curr);
            Random rand = new Random();
            int a = rand.nextInt(c.size());
            c.get(a).addOneInfantry(curr);
            if (!(curr.getTerritories().contains(c.get(a))))
                curr.addTerritory(c.get(a));

            turn.setTurnPlayer(turn.nextTurn(turn.getTurnPlayer()));
            paintComponents(getGraphics());
    }

   // wait(5);
        for(Player p: players) {
            System.out.println();
            System.out.printf("\t%28s\n",p.getName().toUpperCase());
            printList(getTerritoriesOwnedBy(p));

        }
        // console render
        System.out.println();


        java.util.Map<Player,Boolean> noAttackMoves = new HashMap<>();
        for (Player p:players){
            noAttackMoves.put(p,false);
        }

        //============ main game loop =================

        System.out.println("here");


        System.out.println("Here");
       // paint(this.getGraphics());
        //scan.nextLine();
    //    wait(10000);

        while (true) {
            if (attackingCountry != null)
                System.out.println(attackingCountry.getName());
            if (defendingCountry != null)
                System.out.println(defendingCountry.getName());
         //   paintComponents(getGraphics());


            territoriesCapturedThisTurn = 0;    // Keeps track of the number of territories captured in each turn

            // These couple of lines make sure that whenever players are eliminated the dashboard moves
            for (Player p:players
            ) {
                p.setLocation(new Location(20,940-(players.indexOf(p))*35));
            }





            if (doWeHaveAWinner(turn.getTurnPlayer())) {
                System.out.println(turn.getTurnPlayer().getName()+" is the winner!!!");
                System.out.println(players.size());
                break;
            }

            if (players.size() == 1)
            {
                System.out.println(turn.getTurnPlayer() + " wins!!!");
                System.exit(0);
            }


            if (getTerritoriesOwnedBy(turn.getTurnPlayer()).isEmpty()) {
              //  wait(3);
                System.out.println(turn.getTurnPlayer().getName() + " eliminated!");
                players.remove(turn.getTurnPlayer());
             //   wait(3);
                numberOfPlayers--;
                turn.setTurnPlayer(turn.nextTurn(turn.getTurnPlayer()));
                continue;
            }

                int t = (int)Math.floor(getTerritoriesOwnedBy(turn.getTurnPlayer()).size()/3.0);
                turn.getTurnPlayer().setTotalInitialTroops((t<3.0)?3:t);
                System.out.println(turn.getTurnPlayer().getName() + " controls " + getTerritoriesOwnedBy(turn.getTurnPlayer()).size() + " territories, therefore receives " + turn.getTurnPlayer().getTotalInitialTroops() + " troops" );





            //- draft phase
                Random rand = new Random();
                int howmanyTroops = turn.getTurnPlayer().getTotalInitialTroops();
            //    Country randomC = getTerritoriesOwnedBy(turnPlayer).get(rand.nextInt(getTerritoriesOwnedBy(turnPlayer).size()));
    //            System.out.printf("%s is drafting %d new troops to %s\n",turnPlayer.getName(),howmanyTroops,randomC.getName());
              //  draftTroops(randomC,howmanyTroops);


            System.out.println(turn.getTurnPlayer().getName() + " gets " + getContinentOccupationPoints(turn.getTurnPlayer()) + " for occupying continents");
            turn.getTurnPlayer().addToTotalInitialTroops(getContinentOccupationPoints(turn.getTurnPlayer()));

       //     nextPhase();
            System.out.println(turn.getTurnPlayer().getName()+"'s attack Phase");

            List<Country> playerTerritoryThatCanAttack;
            do {
                System.out.println();
                List<Country> playerTerritory = getTerritoriesOwnedBy(turn.getTurnPlayer());

                playerTerritoryThatCanAttack = getContriesPlayerCanAttackFrom(playerTerritory,turn.getTurnPlayer());
                if(!playerTerritoryThatCanAttack.isEmpty())
                    System.out.printf("%s can attack from these countries\n",turn.getTurnPlayer().getName());

                if (playerTerritoryThatCanAttack.isEmpty()) {
                    noAttackMoves.put(turn.getTurnPlayer(),true);
                    break;
                }
                printList(playerTerritoryThatCanAttack);
                System.out.println();
             //   attackingCountry = playerTerritoryThatCanAttack.get(rand.nextInt(playerTerritoryThatCanAttack.size()));


             //   System.out.printf("%s picked as attacking country\n",attackingCountry.getName());



                // randomly pick a country from player's territory (attackingCountry)
                List<Country> neighboringCountryPlayerCanAttackTo = new ArrayList<>();

                if (attackingCountry!= null) {
                    neighboringCountryPlayerCanAttackTo = getNeighboringCountryPlayerCanAttackTo(attackingCountry, turn.getTurnPlayer());
                    System.out.println();
                    if (neighboringCountryPlayerCanAttackTo.isEmpty()) {
                        playerTerritoryThatCanAttack.remove(attackingCountry);
                        System.out.println("Removing " + attackingCountry.getName());
                        break;
                    }
                }
          //      System.out.printf("%s can attack one of these countries \n",attackingCountry.getName());
                printList(neighboringCountryPlayerCanAttackTo);
                System.out.println();
              //  defendingCountry = neighboringCountryPlayerCanAttackTo.get(rand.nextInt(neighboringCountryPlayerCanAttackTo.size()));
             //   System.out.println(defendingCountry.getName() + " picked as defending country.");
                System.out.println();
            //    System.out.printf("%s(%d troops) is now attacking %s (%d troops)\n",attackingCountry.getName(),attackingCountry.getTroops(),defendingCountry.getName(),defendingCountry.getTroops());
                System.out.println();
                nowAttacking = true;
// paintComponents(getGraphics());
                try {


                } catch (Exception e) {}
                if (attackingCountry != null && defendingCountry != null)
                    attack(attackingCountry,defendingCountry);
                try {


                } catch (Exception e) {}

      //       paintComponents(getGraphics());
                try {


                } catch (Exception e) {}

                nowAttacking = false;

            } while (!playerTerritoryThatCanAttack.isEmpty());

            printList(getTerritoriesOwnedBy(turn.getTurnPlayer()));
            System.out.println();
         //   nextPhase();

            if(territoriesCapturedThisTurn >= 1 && turn.getTurnPlayer().cards.size()<6){
                if(cardStack.isEmpty()) {
                    cardStack.addAll(cards);
                }
                Card c = cardStack.pop();
                c.setOwnedBy(turn.getTurnPlayer());
                turn.getTurnPlayer().cards.add(c);

                System.out.println(turn.getTurnPlayer().getName() + " pulled " + c.toString() + " card");

            }

       //     turnPlayer = nextTurn(turnPlayer);

         //   nextPhase();
        }

        // printing the final results
        for(Player p: players) {
            System.out.println();
            System.out.printf("\t%28s\n",p.getName().toUpperCase());
            printList(getTerritoriesOwnedBy(p));
         }
     }
    private boolean doWeHaveAWinner(Player p){
        boolean doWeHaveAWinner = true;
        for (Country c :
                map.countries) {
            if (c.getOwnedBy()!=p) doWeHaveAWinner = false;
        }
        return doWeHaveAWinner;
    }
    public List<Country> getNeighboringCountryPlayerCanAttackTo(Country originCountry, Player player){
        List<Country> country = new ArrayList<>();
        for (Country enemyCountry:originCountry.getNeighbors()
             ) {
            if (canAttackThisTerritory(originCountry,enemyCountry,player)){
                country.add(enemyCountry);
            }
        }
        return country;
    }

    private void draftTroops(Country country,int troops) {
        Player p = country.getOwnedBy();
        for(int i = 0; i<troops; i++) {
            country.addOneInfantry(p);
        }


    }

    private void checkCardPoints(Player p) {
        int countryCardPoint = 0;
        int cavalry = 0;
        int artillery = 0;
        int infantry = 0;
        int specialCard = 0;
        SpecialCard s = new SpecialCard();
        for (Card c:
             p.cards) {

            if (c.getClass() == s.getClass()) {
                specialCard += 1;
            } else {
                if (c.troopsType == Card.TroopsType.INFANTRY)
                    infantry += 1;
                else if (c.troopsType == Card.TroopsType.ARTILLERY)
                    artillery += 1;
                else if (c.troopsType == Card.TroopsType.CAVALRY)
                    cavalry += 1;
            }
        }
        for (Country c:getTerritoriesOwnedBy(p)
             ) {
            for (Card d:
                 p.cards) {
                if (d.getTerritory() == c){
                    countryCardPoint += 2;
                }
            }
        }
        if ((cavalry>=1 && artillery>=1 && infantry>=1)|| cavalry >=3 || artillery>=3 || infantry>= 3 || (infantry>=1 && cavalry >=1 && specialCard >=1) || (cavalry >=1 && artillery >= 1 && specialCard>=1) || (artillery>=1 && infantry>=1 && specialCard>=1)) {
            System.out.println("We have a match");
        }

    }
     private void attack(Country origin, Country destination) {

        // repaint();
    //    Scanner scan = new Scanner(System.in);
        Player attacker = origin.getOwnedBy();
        Player defender = destination.getOwnedBy();
       // Random rand = new Random();
       // System.out.print(origin.getOwner().getName()+", how many dice you want to roll?, max "+((origin.getTroops()-1)>3?3:origin.getTroops()-1)+": ");
  //      int attackDices = scan.nextInt();
         int attackDices = (origin.getTroops()-1)>3?3:(origin.getTroops()-1);
         //System.out.print(destination.getOwner().getName()+", how many dice you want to roll?: max "+((destination.getTroops()>2)?2:1)+": ");
    //     int defenseDices = scan.nextInt();
         int defenseDices = (destination.getTroops()>2)?2:1;
         List<Dice> defenseDiceRolls = new ArrayList<>();
         List<Dice> attackDiceRolls = new ArrayList<>();
         if (origin.getTroops()> attackDices && attackDices <= 3) {
             System.out.printf("%s decided to roll %d dice.\n",attacker.getName(),attackDices);
             attackDiceRolls = attacker.rollDices(attackDices);
             printDice(attackDiceRolls);
         } else {
             System.out.println("Cannot roll " + attackDices + " dices. Not enough troops");

         }
         if (defenseDices == 1 || (defenseDices == 2 && destination.getTroops()>=2)) {

             System.out.printf("%s decided to roll %d dice.\n",defender.getName(),defenseDices);
             defenseDiceRolls = defender.rollDices(defenseDices);
             printDice(defenseDiceRolls);
         } else {
             System.out.println("Cannot roll " + defenseDices + " dice. Not allowed");
         }
         int ik = 0;
         if (defenseDiceRolls.size() > attackDiceRolls.size()) {
             ik = attackDiceRolls.size();
         } else {
             ik = defenseDiceRolls.size();
         }
    //     System.out.println("Comparing dice.");
         int numberAttackersRemoved = 0;
         for (int i = 0; i < ik;i++) {
             if (attackDiceRolls.get(i).getFaceValue() > defenseDiceRolls.get(i).getFaceValue()) {
                 // attack winner
                 System.out.println(attacker.getName()+" wins");
                destination.removeTroops(1);
                if (destination.getTroops() == 0) { // if invaded
                    destination.setOwnedBy(origin.getOwnedBy());
                    // occupy territory
                    System.out.println(origin.getName() + " captures " + destination.getName() + "!!");
                    territoriesCapturedThisTurn += 1;

        //            System.out.print(attacker.getName()+", how many troops do you want to move to the new territory? max " + (origin.getTroops() - 1)+": ");
                    //                  //  int movetroops = scan.nextInt(); // move troops has to be equal or move than attackDices

                    Random rand = new Random();
                    int movetroops = 1 + rand.nextInt(origin.getTroops()-1);

                    System.out.printf("%s decides to move %d troop%s from %s to %s\n",attacker.getName(),movetroops,(movetroops==1)?"":"s",origin.getName(),destination.getName());
                    if (origin.getTroops() - movetroops >= 1) {
                        destination.setTroops(movetroops);
                        destination.setOwnedBy(attacker);
                        attacker.addTerritory(destination);
                        defender.removeTerritory(destination);
                        origin.removeTroops(movetroops);
                    }
                    else
                        System.out.println("Cannot move " + movetroops +". You need to leave at least one troop behind.");
                }
             } else {
                 // defense winner
                 System.out.println(defender.getName() + " wins");
                 if (numberAttackersRemoved<2)
                    origin.removeTroops(1);
                 numberAttackersRemoved += 1;
             }
             map.toString();
         }

         //defender.rollDices(defenseDices);

     }
     private void printDice(List<Dice> list) {
         for (Dice d:list
              ) {
             System.out.printf("   ________\n" +
                     "  /\\       \\\n" +
                     " /  \\  %d    \\\n" +
                     "{    }-------}\n" +
                     " \\  /       /\n" +
                     "  \\/_______/\n",d.getFaceValue());


         }
         System.out.println();
     }
    private boolean canAttackThisTerritory(Country origin, Country destination, Player attacker) {
        // cannot attack your own territories
        boolean c1 = (origin.getOwnedBy() == destination.getOwnedBy()?false:true);

        // cannot attack other territories if you only have one troop
        boolean c2 = (origin.getTroops() <= 1?false:true);

        // can only attack adjacent(touching) countries
        boolean c3 = origin.getNeighbors().contains(destination);

        // cannot attack from someone else's territory
        boolean c4 = origin.getOwnedBy() == attacker;

            return c1 && c2 && c3 && c4;
    }

    /** Does the player occupy one or more continent
     * @param player to see if occpies the continet
     * @return total points for each of the continent occupied
     */
    private int getContinentOccupationPoints(Player player){
        java.util.Map<Country.Continent,Integer> points = new HashMap<>();
        points.put(Country.Continent.SOUTH_AMERICA,2);
        points.put(Country.Continent.NORTH_AMERICA,5);
        points.put(Country.Continent.AFRICA,3);
        points.put(Country.Continent.EUROPE,5);
        points.put(Country.Continent.ASIA,7);
        points.put(Country.Continent.AUSTRALIA,2);

        int tp = 0;
        java.util.Map<Country.Continent,List<Country>> m = new HashMap<>();

        for (Country.Continent continent: // loop through all the continents
        Country.Continent.values()){ //
            List<Country> countries = new ArrayList<Country>();
            for (Country c:
                 map.countries) {
                if (c.continent == continent) {
                    countries.add(c);
                }
            }
            m.put(continent,countries);
            if (countries.containsAll(player.getTerritories())) {
                tp += points.get(continent);
              //  wait(2);
                System.out.println("Entire continent captured!!!");
              //  wait(2);
            }
        }



        return tp;
    }
     private boolean controlsContinent(Player p) {


        for (Country.Continent continet: Country.Continent.values()) {

        }
        return false;
     }

     private Player nextTurn(Player currentPlayer) {
         if (!players.isEmpty()) {
             totalTurnsCounter++;
             // if currentPlayer is last in the list point to the beginning
             if (players.indexOf(currentPlayer) + 1 >= players.size()) {
                 return players.get(0);
             } else {
                 return players.get(players.indexOf(currentPlayer)+1);
             }
         }
            return null;
     }

     private void nextPhase() {
        phase++;
         if (phase >= 3)
             phase = 0;
     }
     private GamePhase getGamePhase() {

        if (phase == 0) return GamePhase.DRAFT;
        else if (phase == 1) return GamePhase.ATTACK;
        else return GamePhase.FORTIFY;
     }
     private List<Country> getTerritoriesOwnedBy(Player p) {
        List<Country> m = new ArrayList<Country>();
        for(Country c:map.countries) {
            if (c.getOwnedBy() == p)
                m.add(c);
        }
        return m;
     }

    private int getNumTerritoriesOwnedBy(Player p) {
        int count = 0;
        for(Country c:map.countries) {
            if (c.getOwnedBy() == p)
                count = count + c.getTroops();
        }
        return count;
    }
     private void printList(List<Country> mc) {
             try {
                 int c = 0;
                 for (Country d : mc) {
                     c+=d.getTroops();
                     System.out.print("\t");
                     System.out.printf("(%2s) %-21s%2s troop%s\n",map.countries.indexOf(d),d.getName(),d.getTroops(),(d.getTroops()==1?"":"s"));
                 }
                 System.out.printf("\t%35.35s\n","---------------------------------------");
                 System.out.printf("\t%28d troops\n",c);

             } catch (Exception e) {
             }

     }
     private int getInitialInfantryCount (int numPlayers) {

        int numInitialInfantryCount = 0;
        if (numPlayers == 2) {
            // special case : will implement later
        } else if (numPlayers == 3) {
            numInitialInfantryCount = 35;
        } else if (numPlayers == 4) {
            numInitialInfantryCount = 30;
        } else if (numPlayers == 5) {
            numInitialInfantryCount = 25;
        } else if (numPlayers == 6) {
            numInitialInfantryCount = 20;
        }
        return  numInitialInfantryCount;
    }

    private void wait(int seconds) {
        try {TimeUnit.SECONDS.sleep(seconds);} catch (Exception e) {}
    }
    private List<Country> getContriesPlayerCanAttackFrom(List<Country>playerTerritory,Player player){
        List<Country> contryPlayerCanAttackFrom = new ArrayList<>();
        for (Country c :
                playerTerritory) {
            boolean add = false;
            for (Country n :
                    c.getNeighbors()) {
                if (n.getOwnedBy() != player) add = true;

            }


            if ((c.getTroops() > 1) && add) contryPlayerCanAttackFrom.add(c);
        }
        return contryPlayerCanAttackFrom;
    }

    private void paintPlayerDashBaord(Graphics g){
        //if (attackingCountry.getTroops()==1) {
          //  attackingCountry = null;
        //}
        numSelector.paintComponents(g);
        if (attackingCountry != null)
            attackingCountry.drawSelected(g,GamePhase.ATTACK);

        if(turn.getGamePhase() == GamePhase.DRAFT) {
            for (Country c:turn.getTurnPlayer().getTerritories()
                 ) {
                c.drawSelected(g,turn.getGamePhase());
            }
        }

        draftBox.setBackgrounColor(Color.gray);
        attackBox.setBackgrounColor(Color.gray);
        fortifyBox.setBackgrounColor(Color.gray);



        if (turn.getGamePhase() == GamePhase.DRAFT) {
            draftBox.setBackgrounColor(turn.getTurnPlayer().getPlayerColor());
        } else if (turn.getGamePhase() == GamePhase.ATTACK) {
            attackBox.setBackgrounColor(turn.getTurnPlayer().getPlayerColor());
        } else {
            fortifyBox.setBackgrounColor(turn.getTurnPlayer().getPlayerColor());
        }
        int playerWidth = 100;
        int playerHeight = 30;


        nextBox.setCoordinate(new Location(870,765));
        nextBox.setDimension(new Dimension(100,playerHeight));
        nextBox.setBackgrounColor(Color.white);
        if (turn.getGamePhase() == GamePhase.FORTIFY) {
            nextBox.setText("DONE");
        } else {
            nextBox.setText("NEXT PHASE");
        }
        g.setColor(Color.darkGray);
        g.fillRect(20+playerWidth+20,940-5*35,845,playerHeight);
        g.setColor(turn.getTurnPlayer().getPlayerColor());
    //    g.fillRect(20+playerWidth+125,940-5*35,200,playerHeight);
        draftBox.setCoordinate(new Location(245,765));
        draftBox.setDimension(new Dimension(200,playerHeight));

        attackBox.setCoordinate(new Location(450,765));
        attackBox.setDimension(new Dimension(200,playerHeight));

        fortifyBox.setCoordinate(new Location(655,765));
        fortifyBox.setDimension(new Dimension(200,playerHeight));




        draftBox.setText("Draft");
        attackBox.setText("Attack");
        fortifyBox.setText("Fortify");

        draftBox.paintComponents(g);
        attackBox.paintComponents(g);
        fortifyBox.paintComponents(g);
        nextBox.paintComponents(g);


     //   g.fillRect(20+playerWidth+330,940-5*35,200,playerHeight);

     //   g.fillRect(20+playerWidth+535,940-5*35,200,playerHeight);

        g.setColor(Color.darkGray);
        g.fillRect(20+playerWidth+20,940-4*35,845,205-35);
       // p.setLocation(new Location(20,);
        for(int i = 0; i<6; i++) {
            g.setColor(Color.darkGray);
            g.fillRect(20,940-i*35,playerWidth,playerHeight);
            g.fillRect(20+playerWidth+5,940-i*35, 10, playerHeight);

        }


        for (Player p: players
        ) {


            g.setColor(p.getPlayerColor());

            g.fillRect(p.getLocation().getX(),p.getLocation().getY(),playerWidth,playerHeight);

            if (turn.getTurnPlayer() == p) {
                //    g.setColor(Color.black);
                g.fillRect(p.getLocation().getX()+playerWidth+5, p.getLocation().getY(), 10, playerHeight);
            }

            if (p.getPlayerColor() == Color.blue)
                g.setColor(Color.white);
            else
                g.setColor(Color.black);
            g.drawString(p.getName()+"",p.getLocation().getX()+5,p.getLocation().getY()+20);
            String num = String.valueOf(getNumTerritoriesOwnedBy(p));
            g.drawString(num,p.getLocation().getX()+playerWidth-8-(num.length()*8),p.getLocation().getY()+20);
            g.setColor(Color.white);

            for (Card c:turn.getTurnPlayer().cards){
                //g.fillRect(140,800,120,170);
                c.setCoordinate(new Location(140+turn.getTurnPlayer().cards.indexOf(c)*140,800));
                c.paintComponents(g);
            }
        }
    }

    private void animate(Graphics g) {
        for (Country c:
                map.countries) {
            c.paintComponents(g);
        }
        paintPlayerDashBaord(g);
    }

    @Override
    public void paintComponents(Graphics g) {
        //super.paintComponents(g);
        //animate(g);
                try {
            BufferedImage bf = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);

            animate(bf.getGraphics());
            g.drawImage(bf, 0, 0, Color.white, this);

        } catch (Exception e) {System.out.println("Error here");}

    }

    public Map loadMap() {
        map = new Map();
        cards = new ArrayList<Card>();
        cardStack = new Stack<>();
        for (Country c:map.countries
             ) {
            cards.add(new Card(c, Card.TroopsType.ARTILLERY));
        }

        return map;
    }
}
