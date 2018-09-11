package com.company;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {
    private int maxPlayers = 6;
    private int minPlayers = 2;
    private int initialInfantryCount = 0;
    public Map map;
    boolean show = false;
    public List<Player> players;
    public Dice gameDice;
    private Stack<Card> cardStack;
    public List<Card> cards;
    public Player turnPlayer;
   // private int turn = 0;
    private static int totalTurnsCounter = 0;
    private int phase = 0;
    private int numberOfPlayers = 0;
    private int setsOfRiskCardsTraded = 0;
    Country attackingCountry;
    Country defendingCountry;
    boolean nowAttacking = false;
    int territoriesCapturedThisTurn;
    public enum GamePhase {
        DRAFT, ATTACK, FORTIFY
    }


    // initialize map
    public Game() {
        //super.paint();
     //   System.out.println("rendering");
        // write your code here



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
        playerColors.add(Color.red);
        playerColors.add(Color.blue);
        playerColors.add(Color.green);
        playerColors.add(Color.orange);
        playerColors.add(Color.yellow);
        playerColors.add(Color.pink);

        Collections.shuffle(playerColors);
        //load map
        map = loadMap();

        attackingCountry=map.countries.get(0);

        defendingCountry=map.countries.get(1);

        // create players for test purpose
        players = new ArrayList<Player>();
        players.add(new Player("Navin"));
        players.add(new Player("Sunada"));
        players.add(new Player("Jack"));
        players.add(new Player("John"));
        players.add(new Player("Jim"));
        players.add(new Player("Mark"));





        Collections.shuffle(cards);
        for (Card c:
             cards) {
            System.out.println(c);
        }
        try {
     //       Thread.sleep(20000);
        } catch (Exception e) {}
        cardStack.clear();
        cardStack.addAll(cards);

        System.out.println("Cards count: "+cardStack.size());


        for (Player p:players
        ) {
            p.setPlayerColor(playerColors.get(players.indexOf(p)));
            p.setLocation(new Location((20+(players.indexOf(p))*160),800));

            p.setCapital(cardStack.pop().getTerritory());
            System.out.println(p.getCapital().getName());
        }

        System.out.println("Cards count: "+cardStack.size());
        cards.clear();
        cards.addAll(cardStack);

        Card c43 = new SpecialCard(Card.TroopsType.INFANTRY, Card.TroopsType.CAVALRY, Card.TroopsType.ARTILLERY);
        Card c44 = new SpecialCard(Card.TroopsType.INFANTRY, Card.TroopsType.CAVALRY, Card.TroopsType.ARTILLERY);
        cards.add(c43);
        cards.add(c44);
        Collections.shuffle(cards);

        cardStack.removeAllElements();
        cardStack.addAll(cards);

        System.out.println("Cardstack card count " + cardStack.size());






        numberOfPlayers = players.size();
        Collections.shuffle(players);
        initialInfantryCount = getInitialInfantryCount(numberOfPlayers) * numberOfPlayers;



        // calculate and assign number of infantry at the beginning
        for(Player p: players) {
            int a = getInitialInfantryCount(players.size());
            p.setTotalInitialTroops(a);
        }
        System.out.println("We have " + players.size() + " players!");

//        System.out.println("Each players now rolling dice to determine who is going first.");
//
//        // roll dice
//        Player highestPlayer = players.get(0);
//        for(Player p: players) {
//
//            //System.out.print(p.getName() + " rolled " );
//            System.out.printf("%s is rolling dice ...\n",p.getName());
//            printDice(p.rollDices(1));
//            if (p.dice.get(0).getFaceValue() > highestPlayer.dice.get(0).getFaceValue())
//                highestPlayer = p;
////            for(Dice d: p.dice) {
////                System.out.print(d.getFaceValue() + " ");
////            }
////            System.out.println();
//        }
//        turn = players.indexOf(highestPlayer);
//        System.out.println();
//        System.out.println(highestPlayer.getName() + " is going first");







        turnPlayer = players.get(0); //
        System.out.println(turnPlayer.getName()+" going first.");

        ArrayList<Integer> randomCountries = new ArrayList<Integer>();
        for (int i= 0; i < map.countries.size(); i++) {
            randomCountries.add(i);

        }
        Collections.shuffle(randomCountries);

         for (int i= 0; i < 42; i++) {
            map.countries.get(randomCountries.get(i)).addOneInfantry(turnPlayer);
            turnPlayer.addTerritory(map.countries.get(randomCountries.get(i)));
            turnPlayer = nextTurn(turnPlayer);
        }
        for (int i = 0; i < initialInfantryCount - 42; i++) {
            Player curr = turnPlayer;
            List<Country> c = getTerritoriesOwnedBy(curr);
            Random rand = new Random();
            int a = rand.nextInt(c.size());
            c.get(a).addOneInfantry(curr);
            if (!(curr.getTerritories().contains(c.get(a))))
                curr.addTerritory(c.get(a));
            turnPlayer = nextTurn(turnPlayer);
        }

     // System.out.println("Randomly populating the map ... ");
        for(Player p: players) {
            System.out.println();
            System.out.printf("\t%28s\n",p.getName().toUpperCase());
            printList(getTerritoriesOwnedBy(p));
     //       System.out.println(" - " + getNumTerritoriesOwnedBy(p) + " troops across " +getTerritoriesOwnedBy(p).size() + " countries ");

        }
        // console render
        System.out.println();
    //    System.out.println("---------------------------snapshot of the map-------------------------------------------------------------------------------------");

  //      System.out.println(map.toString());
    //    System.out.println(map.getTotalTroops() +" troops across 42 countries, 6 continents");


//        for (Card c:cards) {
//            System.out.println((c.getClass() == new SpecialCard().getClass())?"Special":"Not so special");
//        }

        java.util.Map<Player,Boolean> noAttackMoves = new HashMap<>();
        for (Player p:players){
            noAttackMoves.put(p,false);
        }
        Scanner scan = new Scanner(System.in);
        //============ main game loop =================
        turnPlayer = players.get(0);


        InfoBox ifb = new InfoBox();


//        wait(200);
        while (true) {
           // wait(100);
            territoriesCapturedThisTurn = 0;
            checkCardPoints(turnPlayer);
            try {

            } catch (Exception ex)  {
            }
            paint(this.getGraphics());


          //  try {TimeUnit.MILLISECONDS.sleep(300);} catch (Exception e) {}
            boolean doWeHaveAWinner = true;
            System.out.println(players.size());
            for (Country c :
                    map.countries) {
                if (c.getOwner()!=turnPlayer) doWeHaveAWinner = false;
            }
            if (doWeHaveAWinner) {
                System.out.println(turnPlayer.getName()+" is the winner!!!");
                System.out.println(players.size());
                break;
            }

            if (players.size() == 1)
            {
                System.out.println(turnPlayer + " wins!!!");
                System.exit(0);
            }


            if (getTerritoriesOwnedBy(turnPlayer).isEmpty()) {

              //  wait(3);
                System.out.println(turnPlayer.getName() + " eliminated!");
                players.remove(turnPlayer);
             //   wait(3);
                numberOfPlayers--;
                turnPlayer = nextTurn(turnPlayer);
                continue;
            }


//            boolean exitNow = true;
//            for(Player p:players){
//                System.out.println(p.getName() + " " +noAttackMoves.get(p));
//                //System.out.println(noAttackMoves.get(i) + " for "+players.get(i).getName());
//                if (noAttackMoves.get(p) == false)
//                    exitNow = false;
//            }
//            if (exitNow) {
//                System.out.println();
//                System.out.println("Game is now at a stalemate!! That's what you want in a successful simulation. It took "+totalTurnsCounter+" turns for the game to achieve stalemate.");
//                System.out.println();
//                System.out.print("\u2714");
//              //  render(map);
//                break;
//            }

            //-----------------calculate bonus troops
            // draft new troops
                // get number of territories player occupies
                Player player = turnPlayer;


                System.out.println();
                System.out.println("---------------------------------------");
                System.out.println(player.getName().toUpperCase()+"'s TURN ("+players.indexOf(turnPlayer)+")");
                System.out.println("---------------------------------------");
                int t = (int)Math.floor(getTerritoriesOwnedBy(player).size()/3.0);
                player.setTotalInitialTroops((t<3.0)?3:t);
                System.out.println(player.getName() + " controls " + getTerritoriesOwnedBy(player).size() + " territories, therefore receives " + player.getTotalInitialTroops() + " troops" );




             //   System.out.println(player.getTotalInitialTroops());
                // does this player control a continent , if so add respective value
                // check matched RISK cards from a set of 3 cards this player has accumulated

            //- draft phase
                Random rand = new Random();
                int howmanyTroops = player.getTotalInitialTroops();
                Country randomC = getTerritoriesOwnedBy(player).get(rand.nextInt(getTerritoriesOwnedBy(player).size()));
                System.out.printf("%s is drafting %d new troops to %s\n",player.getName(),howmanyTroops,randomC.getName());
                draftTroops(randomC,howmanyTroops);


            System.out.println(player.getName() + " gets " + getContinentOccupationPoints(player) + " for occupying continents");
            turnPlayer.addToTotalInitialTroops(getContinentOccupationPoints(player));

            nextPhase();
            System.out.println(player.getName()+"'s attack Phase");
        // attack phase
           // ------------------------------------
            //Pick country to attack from
                // get territories of current player
            List<Country> playerTerritoryThatCanAttack;
            do {
                System.out.println();
                List<Country> playerTerritory = getTerritoriesOwnedBy(player);
               // System.out.println("Countries owned by " + player.getName());
               // printList(playerTerritory);


                // list of countries with more than 1 troop
               // System.out.println();


                playerTerritoryThatCanAttack = getContriesPlayerCanAttackFrom(playerTerritory,player);
                if(!playerTerritoryThatCanAttack.isEmpty())
                    System.out.printf("%s can attack from these countries\n",player.getName());

                if (playerTerritoryThatCanAttack.isEmpty()) {
                    noAttackMoves.put(turnPlayer,true);
      //              turnPlayer = nextTurn(turnPlayer);
                    break;
                }
  //              printList(playerTerritoryThatCanAttack);
                printList(playerTerritoryThatCanAttack);
                System.out.println();
                attackingCountry = playerTerritoryThatCanAttack.get(rand.nextInt(playerTerritoryThatCanAttack.size()));

                System.out.printf("%s picked as attacking country\n",attackingCountry.getName());



                // randomly pick a country from player's territory (attackingCountry)
                List<Country> neighboringCountryPlayerCanAttackTo = new ArrayList<>();


                neighboringCountryPlayerCanAttackTo = getNeighboringCountryPlayerCanAttackTo(attackingCountry,player);
                System.out.println();
                if (neighboringCountryPlayerCanAttackTo.isEmpty()) {
                    playerTerritoryThatCanAttack.remove(attackingCountry);
                    System.out.println("Removing " + attackingCountry.getName());
                    break;
                }


     //           System.out.println("All neighboring countries of "+attackingCountry.getName());
     //           printList(attackingCountry.getNeighbors());
     //           System.out.println();
                System.out.printf("%s can attack one of these countries \n",attackingCountry.getName());
                printList(neighboringCountryPlayerCanAttackTo);
                System.out.println();
                defendingCountry = neighboringCountryPlayerCanAttackTo.get(rand.nextInt(neighboringCountryPlayerCanAttackTo.size()));
                System.out.println(defendingCountry.getName() + " picked as defending country.");
                System.out.println();
                System.out.printf("%s(%d troops) is now attacking %s (%d troops)\n",attackingCountry.getName(),attackingCountry.getTroops(),defendingCountry.getName(),defendingCountry.getTroops());
                System.out.println();
//               / wait(1);
                nowAttacking = true;
            //    try {TimeUnit.MILLISECONDS.sleep(5);} catch (Exception e) {};
                repaint();

                try {

      //              Thread.sleep(20);

                } catch (Exception e) {}
            //    wait(1);
                attack(attackingCountry,defendingCountry);
                try {

       //             Thread.sleep(20);

                } catch (Exception e) {}
                repaint();
                try {

        //            Thread.sleep(20);

                } catch (Exception e) {}



                //  wait(1);
                nowAttacking = false;

            } while (!playerTerritoryThatCanAttack.isEmpty());

                //


            printList(getTerritoriesOwnedBy(player));
            System.out.println();
         //   System.out.println("We are done here. Final territory");
           // noAttackMoves[turn] = true;


           // scan.nextLine();

            // -----------------------


            ;
            nextPhase();
//            System.out.println("Draft Phase:");
//            List<Country> turnPlayerTerritories = getTerritoriesOwnedBy(turnPlayer);
//            int numTerritory = turnPlayerTerritories.size();
//            Country originT = turnPlayerTerritories.get(rand.nextInt(numTerritory));
//            Country destinT;
//            do {
//                destinT = turnPlayerTerritories.get(rand.nextInt(numTerritory));
//            } while(originT==destinT);
//            int numT = (originT.getTroops()-1<=0?1:originT.getTroops()-1);
//       //     moveTroops(originT,destinT,1+rand.nextInt(numT));
//         //   wait(2  );
//






//            for (int i =0; i<numberOfPlayers;i++)
//                nextTurn();

            if(territoriesCapturedThisTurn >= 1){
                if(cardStack.isEmpty()) {
                    cardStack.addAll(cards);
                }
                Card c = cardStack.pop();
                turnPlayer.cards.add(c);
                System.out.println(turnPlayer.getName() + " pulled " + c.toString() + " card");
//                for (Card car :
//                        turnPlayer.cards) {
//                    System.out.println(car.toString());
//                }

              //  wait(2);
            }

            turnPlayer = nextTurn(turnPlayer);

            nextPhase();
        }
        // printing the final results
        for(Player p: players) {
            System.out.println();
            System.out.printf("\t%28s\n",p.getName().toUpperCase());
            printList(getTerritoriesOwnedBy(p));
         }
     //   System.out.print("Want to view gamemap? (1/0): ");
     //   int response = scan.nextInt();
     //   System.out.println(response.toUpperCase());
       // if (response == 1) render();
        render();
     }

    List<Country> getNeighboringCountryPlayerCanAttackTo(Country originCountry, Player player){
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
        Player p = country.getOwner();
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
         //   wait(2);
            System.out.println("We have a match");
       //     wait(2);
        }

    }
     private void attack(Country origin, Country destination) {

        // repaint();
        Scanner scan = new Scanner(System.in);
        Player attacker = origin.getOwner();
        Player defender = destination.getOwner();
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
                    destination.setOwner(origin.getOwner());
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
                        destination.setOwner(attacker);
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
        boolean c1 = (origin.getOwner() == destination.getOwner()?false:true);

        // cannot attack other territories if you only have one troop
        boolean c2 = (origin.getTroops() <= 1?false:true);

        // can only attack adjacent(touching) countries
        boolean c3 = origin.getNeighbors().contains(destination);

        // cannot attack from someone else's territory
        boolean c4 = origin.getOwner() == attacker;

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


     private boolean isOccupied(Country country) {
         for (Country c: map.countries) {
             if (c.getName() == country.getName())
                return (c.getTroops() != 0);
         }
         return  false;
     }
     private boolean attack(Country c , Player p) {


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
            if (c.getOwner() == p)
                m.add(c);
        }
        return m;
     }

    private int getNumTerritoriesOwnedBy(Player p) {
        int count = 0;
        for(Country c:map.countries) {
            if (c.getOwner() == p)
                count = count + c.getTroops();
        }
        return count;
    }
     private void printList(List<Country> mc) {
             try {
//                 for (Country d : mc) {
//                    System.out.println(d.getName() + " ( owner : "+
//                             ((d.getOwner() != null)?d.getOwner().getName():"X") + " troops : " + d.getTroops() + ")"
//                             + "'s neighbors --> ");
//                     for (Country c : d.getNeighbors()) {
//                         System.out.println(c.getName() + " | ");
//                     }
//                     System.out.println();
//                 }
                 int c = 0;



                 for (Country d : mc) {
                     c+=d.getTroops();
                     System.out.print("\t");
                     System.out.printf("(%2s) %-21s%2s troop%s\n",map.countries.indexOf(d),d.getName(),d.getTroops(),(d.getTroops()==1?"":"s"));
                     //System.out.println(d.getName()+ "("+map.countries.indexOf(d)+") : " +d.continent + " (troop/s: " + d.getTroops() +") " );
//                     for (Country c : d.getNeighbors()) {
//                         System.out.println(c.getName() + " | ");
//                     }
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
    //if
    private void moveTroops(Country o, Country d, int numberOfTroops) {
        d.addTroops(numberOfTroops);
        o.removeTroops(numberOfTroops);

    }


    private boolean canAttack(Player player) {
        //player can only attack from countries with more than one troops


        return false;
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
                if (n.getOwner() != player) add = true;

            }


            if ((c.getTroops() > 1) && add) contryPlayerCanAttackFrom.add(c);
            //





            //

        }
        return contryPlayerCanAttackFrom;
    }

    private void render(){



    }

    private void animate(Graphics g) {
        int playerWidth = 150;
        int playerHeight = 30;
        for (Player p: players
        ) {

            g.setColor(p.getPlayerColor());

            g.fillRect(p.getLocation().getX(),p.getLocation().getY(),playerWidth,playerHeight);

            if (turnPlayer == p) {
            //    g.setColor(Color.black);
                g.fillRect(p.getLocation().getX(), 780, playerWidth, 10);
            }




            if (p.getPlayerColor() == Color.blue)
                g.setColor(Color.white);
            else
                g.setColor(Color.black);
            g.drawString(p.getName()+"",p.getLocation().getX()+5,p.getLocation().getY()+20);
            String num = String.valueOf(getNumTerritoriesOwnedBy(p));
            g.drawString(num,p.getLocation().getX()+playerWidth-8-(num.length()*8),p.getLocation().getY()+20);
            g.setColor(Color.white);
//            for (Card c :
//
//                    p.cards) {
//                Location l = p.getLocation();
//                int cardWidth = ((playerWidth-21)/3);
//                int cardHeight = 60;
//                int indexofCurrentCard = p.cards.indexOf(c)+1;
//                g.fillRect(l.getX()+6+indexofCurrentCard*cardHeight,l.getY()+playerHeight+10,cardWidth,cardHeight);
//            }

            g.drawString(p.cards.size() + "",p.getLocation().getX(),p.getLocation().getY()+100);
            
        }
        for (Country c : map.countries
        ) {
            g.setColor(c.getOwner().getPlayerColor());

//            for (Country d:c.getNeighbors()
//            ) {
//                if (d.getOwner() == c.getNeighbors());
//                g.drawLine(d.getCoordinate().getCenter(100).getX(),d.getCoordinate().getCenter(100).getY(),c.getCoordinate().getCenter(100).getX(),c.getCoordinate().getCenter(100).getY());
//            }

            g.fillRect(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getDimension().getWidth(), c.getDimension().getHeight());


            g.setColor(Color.black);
            g.drawRect(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getDimension().getWidth(), c.getDimension().getHeight());
            g.setColor(c.getOwner().getPlayerColor());

            if (c.getOwner().getPlayerColor() == Color.blue)
                g.setColor(Color.white);
            else
                g.setColor(Color.black);


            int x = c.getCoordinate().getCenter(c.getDimension().getWidth(),c.getDimension().getHeight()).getX();

            int y= c.getCoordinate().getCenter(c.getDimension().getWidth(),c.getDimension().getHeight()).getY();

     //       g.drawString(c.getName(), c.getCoordinate().getX()+5, c.getCoordinate().getCenter(c.getDimension().getWidth(), c.getDimension().getHeight()).getY()-10);
           // g.setFont(new Font("TimesRoman",Font.PLAIN,18));

//     /       g.drawString(c.getTroops()+"",x,y+10);
     //       g.drawString(c.getOwner().getName(), c.getCoordinate().getX()+5, c.getCoordinate().getCenter(c.getDimension().getWidth(), c.getDimension().getHeight()).getY());
    //        g.fillOval(x-15,y-15,30,30);
      //      g.drawString(getNumTerritoriesOwnedBy(turnPlayer)+"",x,y);

        }
        g.setColor(Color.white);
        int centerX1 = attackingCountry.getCoordinate().getCenter(attackingCountry.getDimension().getWidth(),attackingCountry.getDimension().getHeight()).getX();

        int centerY1= attackingCountry.getCoordinate().getCenter(attackingCountry.getDimension().getWidth(),attackingCountry.getDimension().getHeight()).getY();

        int centerX2 = defendingCountry.getCoordinate().getCenter(defendingCountry.getDimension().getWidth(),defendingCountry.getDimension().getHeight()).getX();
        int centerY2= defendingCountry.getCoordinate().getCenter(defendingCountry.getDimension().getWidth(),defendingCountry.getDimension().getHeight()).getY();

        int cirlceSize = 38;
        Graphics2D g2d = (Graphics2D)g;

        g2d.setStroke(new BasicStroke(5,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND));
        g.setColor(Color.white);
        g.drawLine(centerX1,centerY1,centerX2,centerY2);
        g2d.setStroke(new BasicStroke(1));
        g.setColor(Color.black);
        g.drawLine(centerX1,centerY1,centerX2,centerY2);


        // rattackingoval
        g.setColor(Color.green);
        g.fillOval(centerX1-cirlceSize/2,centerY1-cirlceSize/2,cirlceSize,cirlceSize);

        //defending oval
        g.setColor(Color.red);
        g.fillOval(centerX2-cirlceSize/2,centerY2-cirlceSize/2,cirlceSize,cirlceSize);

        g.setColor(Color.white);
        g.drawOval(centerX1-cirlceSize/2,centerY1-cirlceSize/2,cirlceSize,cirlceSize);

        g.setColor(Color.black);
        g.drawOval(centerX1-cirlceSize/2,centerY1-cirlceSize/2,cirlceSize,cirlceSize);


        g.setColor(Color.white);
        g.drawOval(centerX2-cirlceSize/2,centerY2-cirlceSize/2,cirlceSize,cirlceSize);

//        g.setColor(Color.black);
//        g.drawOval(centerX1-cirlceSize/2,centerY1-cirlceSize/2,cirlceSize,cirlceSize);

        cirlceSize = 30;
        g.setColor(Color.black);
        g.drawOval(centerX1-cirlceSize/2,centerY1-cirlceSize/2,cirlceSize,cirlceSize);
        g.setColor(Color.black);
        g.drawOval(centerX1-cirlceSize/2,centerY1-cirlceSize/2,cirlceSize,cirlceSize);

        g.setColor(Color.white);
        g.drawOval(centerX2-cirlceSize/2,centerY2-cirlceSize/2,cirlceSize,cirlceSize);
        g.setColor(Color.black);
        g.drawOval(centerX1-cirlceSize/2,centerY1-cirlceSize/2,cirlceSize,cirlceSize);


        for (Country c :
                map.countries) {
            if (c.equals(attackingCountry))
                g.setColor(Color.black);
            else if (c.equals(defendingCountry))
                g.setColor(Color.white);
            else
                g.setColor(Color.black);

            int x = c.getCoordinate().getCenter(c.getDimension().getWidth(),c.getDimension().getHeight()).getX();

            int y= c.getCoordinate().getCenter(c.getDimension().getWidth(),c.getDimension().getHeight()).getY();
            String num = String.valueOf(c.getTroops());
            g.drawString(num,x-(num.length()*3),y+5);


        }



    }
    @Override
    public void paint(Graphics g) {
     //   super.paint(g);

        BufferedImage bf = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_RGB);

        animate(bf.getGraphics());
        g.drawImage(bf,0,0,Color.white,this);


    }

    public Map loadMap() {
        map = new Map();
        cards = new ArrayList<Card>();
        cardStack = new Stack<>();
        Card c1 = new Card(new Country("Afganistan"), Card.TroopsType.CAVALRY);
        Card c2 = new Card(new Country("Alaska"), Card.TroopsType.INFANTRY);
        Card c3 = new Card(new Country("Alberta"), Card.TroopsType.CAVALRY);
        Card c4 = new Card(new Country("Argentina"), Card.TroopsType.INFANTRY);
        Card c5 = new Card(new Country("Brazil"), Card.TroopsType.ARTILLERY);
        Card c6 = new Card(new Country("Central Africa"), Card.TroopsType.INFANTRY);
        Card c7 = new Card(new Country("Central America"), Card.TroopsType.ARTILLERY);
        Card c8 = new Card(new Country("China"), Card.TroopsType.INFANTRY);
        Card c9 = new Card(new Country("East Africa"), Card.TroopsType.INFANTRY);
        Card c10 = new Card(new Country("Eastern Australia"), Card.TroopsType.ARTILLERY);
        Card c11 = new Card(new Country("Quebec"), Card.TroopsType.CAVALRY);
        Card c12 = new Card(new Country("Eastern United States"), Card.TroopsType.ARTILLERY);
        Card c13 = new Card(new Country("Egypt"), Card.TroopsType.INFANTRY);
        Card c14 = new Card(new Country("Great Britain"), Card.TroopsType.ARTILLERY);
        Card c15 = new Card(new Country("Greenland"), Card.TroopsType.CAVALRY);
        Card c16 = new Card(new Country("Iceland"), Card.TroopsType.INFANTRY);
        Card c17 = new Card(new Country("India"), Card.TroopsType.CAVALRY);
        Card c18 = new Card(new Country("Indonesia"), Card.TroopsType.ARTILLERY);
        Card c19 = new Card(new Country("Irkutsk"), Card.TroopsType.CAVALRY);
        Card c20 = new Card(new Country("Japan"), Card.TroopsType.ARTILLERY);
        Card c21 = new Card(new Country("Kamchatka"), Card.TroopsType.INFANTRY);
        Card c22 = new Card(new Country("Madagascar"), Card.TroopsType.CAVALRY);
        Card c23 = new Card(new Country("Middle East"), Card.TroopsType.INFANTRY);
        Card c24 = new Card(new Country("Mongolia"), Card.TroopsType.INFANTRY);
        Card c25 = new Card(new Country("New Guinea"), Card.TroopsType.INFANTRY);
        Card c26 = new Card(new Country("North Africa"), Card.TroopsType.CAVALRY);
        Card c27 = new Card(new Country("Northern Europe"), Card.TroopsType.ARTILLERY);
        Card c28 = new Card(new Country("Northwest Territory"), Card.TroopsType.ARTILLERY);
        Card c29 = new Card(new Country("Ontario"), Card.TroopsType.CAVALRY);
        Card c30 = new Card(new Country("Peru"), Card.TroopsType.INFANTRY);
        Card c31 = new Card(new Country("Russia"), Card.TroopsType.CAVALRY);
        Card c32 = new Card(new Country("Scandinavia"), Card.TroopsType.CAVALRY);
        Card c33 = new Card(new Country("Siberia"), Card.TroopsType.CAVALRY);
        Card c34 = new Card(new Country("South Africa"), Card.TroopsType.ARTILLERY);
        Card c35 = new Card(new Country("Southeast Asia"), Card.TroopsType.INFANTRY);
        Card c36 = new Card(new Country("Southern Europe"), Card.TroopsType.ARTILLERY);
        Card c37 = new Card(new Country("Ural"), Card.TroopsType.CAVALRY);
        Card c38 = new Card(new Country("Venezuela"), Card.TroopsType.INFANTRY);
        Card c39 = new Card(new Country("Western Australia"), Card.TroopsType.ARTILLERY);
        Card c40 = new Card(new Country("Western Europe"), Card.TroopsType.ARTILLERY);
        Card c41 = new Card(new Country("Western United States"), Card.TroopsType.ARTILLERY);
        Card c42 = new Card(new Country("Yakutsk"), Card.TroopsType.CAVALRY);

        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);
        cards.add(c5);
        cards.add(c6);
        cards.add(c7);
        cards.add(c8);
        cards.add(c9);
        cards.add(c10);
        cards.add(c11);
        cards.add(c12);
        cards.add(c13);
        cards.add(c14);
        cards.add(c15);
        cards.add(c16);
        cards.add(c17);
        cards.add(c18);
        cards.add(c19);
        cards.add(c20);
        cards.add(c21);
        cards.add(c22);
        cards.add(c23);
        cards.add(c24);
        cards.add(c25);
        cards.add(c26);
        cards.add(c27);
        cards.add(c28);
        cards.add(c29);
        cards.add(c30);
        cards.add(c31);
        cards.add(c32);
        cards.add(c33);
        cards.add(c34);
        cards.add(c35);
        cards.add(c36);
        cards.add(c37);
        cards.add(c38);
        cards.add(c39);
        cards.add(c40);
        cards.add(c41);
        cards.add(c42);

        return map;
    }
    private void render(Map map){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println("Error clearing screen");}
System.out.printf(" \n" +
        "                                                                                                                                                                                                                                                                                         +--------------------+\n" +
        "                                                                                                                                                                                                                                     +-----------------------------+---------------------+                    |\n" +
        "                                                                                                                                                                                                                         +-----------+                             |                     |                    | <--+  Alaska\n" +
        "                                              +----+                              +------------------+                                                                                                                   |                                         |                     |                    |\n" +
        "                                              |    | <--------------------------> |                  |                                                                                                           +------++                                    +----+    Yakutsk          |                    |\n" +
        "                                              |    |                              |                  |                                                                                                           |      |                                     |                          |                +---+\n" +
        "               +------------+-----------------+    |           +----------------> |  Greenland       |                                         +-------------------+                                             |      |                                     |                  +-------+                |\n" +
        "               |            |                      |           |                  |                  |       +--------------+                  |                   +------------+            +-------------------+      |                                     |                  |                        |\n" +
        "Kamchatka +--> |   Alaska   | Northwest Territory  +------+    |  +-----------+   |+                 |       |              |                  |                   |            |            |                   |      |                                     |                  |                        |\n" +
        "               |%12.12s|                      |      | <--+  |           |    +----+            |       |              |        +---------+                   |            +------------+                   |      |                                     |                  |          Kamchatka     |\n" +
        "               +-----+------+-------------------+--+      +-- ----+           |         |            |       |              |        |    Scandinavia              |                                             |      +----+                                +------------------+                        |\n" +
        "                     |                          |                 |  Quebec   |         |            |       |   Iceland    | <----> |                 +----+      |                                             |           |               Siberia          |                  |                        |\n" +
        "                     |           Alberta        |                 |           |         |            | <---> |              |        |                 |    |      |                                             |           |                            +---+                  |                        |\n" +
        "                     |                          |    Ontario      |           | <-----> |            |  +--> |              |        |                 |    |      |                                             |           |                            |        Irkutsk       +-+                      |\n" +
        "                     +------+                   |                 |           |         |     +------+  |    +--------------+        +----+         +--+    +------+                                             |           +-------+                    |                        |         -------+     |\n" +
        "                            |                   |                 |           |         |     |         |                      +--------> |         |                                                            |                   |                    |                        |         |      |     |\n" +
        "                    +-------+--------------+----+-----------------+----+------+         |     |         |   +-----+            |          +--+----+-+                                                            |    Ural           |                    |                        |         |      |     |\n" +
        "                    |                      |                           |                +-----+         |   |     |            |             |    |                                                              |                   |              +-----+------------------------+--+      |      |     |\n" +
        "                    |                      |                           |                                +-> |     | <----------+             |    |                                                              |                   +----+         |                                 |      |      |     |\n" +
        "                    |    Western United    |                           |                                    |     |                +---------+    |                                                +-------------+------+                 |         |                                 |      |      +-----+\n" +
        "                    |        States        |         Easter United     |                              +-----+     +--+             |              |                                                |                    |                 |         |                                 |      |\n" +
        "                    |                      |             States        |                              |              |             |              |                       Ukraine                  |                    |                 |         |                                 |      |             +----------+\n" +
        "                    |                      |                           |                        +---> |Great Britain | <---------> |              +---+                                            |                    |                 +---------+               Mongolia          |      |  <--------> |          |\n" +
        "                    +----+-----------------+                           |                        |     |              |             |                  |                                            |                    +------------+----+         |                                 |      |             |          |\n" +
        "                         |                 |                           |                        |     |              |     +-------+                  |                                            |                                 |              |                                 ------++             |          |\n" +
        "                         |                 +-------------+--------+    |                        |     +---------+    |     |                          |                                            |                                 |              +----------+                            |              |          |\n" +
        "                         |                               |        |    |                        |               |    |     |    Northern Europe       |                                            |             Afganistan          |                         |                            |              |          |\n" +
        "                         |                               |        +----+                        |               +----+     |                          |                                            |                                 |                         +-----------+          ------+              |  Japan   |\n" +
        "                         |+        Central America       |                                      |                  +-------+--------+-----------------+----+                                       |                                 |                                     |          |                    |          |\n" +
        "                          |                              |                                      |                  |                |                      |                                       |                                 |                                     |          |  <---------->  +---+          |\n" +
        "                          +-----+                        |                                      +----------------> | Western Europe |    Southern Europe   |                                       |                                 |                                     +-----+----+                |              |\n" +
        "                                |                        |                                                         |                |                      |                                       |                                 |                                           |                     |              |\n" +
        "                                +----+                   |                                                         |           |----+-----+                +------------------------+              |                            -----+                China                      |                     |         +----+\n" +
        "                                     |                   +------+                                                +--           |          |                |                        |              |                            |                                                |                     |         |\n" +
        "                                     +-----+                    |                                                |             |          |                |                        |              +-------------+--------------+                                                |                     |         |\n" +
        "                                            +                   |                                                |             |          +---+     +------+                        |              |             |              |                                                |                     +---------+\n" +
        "                                            +-----------+       |                                                |           +-+              |     |      +----------+             +--------------+         +---+              --------------+                                 ++\n" +
        "                                                        |       |  +------------------------+                    |           |                |     | <--+            |                                      |                                |                                 |\n" +
        "                                                        |       |  |   Venezuela            |                    +--------+  |                +-----+    |            |                                      |                                |                                 |\n" +
        "                                                        |       +--+                        |                             |  |                           |            |         Middle East                  |                                |                               +-+\n" +
        "                                                        |          |              +---------+------------+                +--+------------+              |            |                                      |                                |                               |\n" +
        "                                                        +----------+              |                      |                |               +--------+     |            |                                      |                                 +                              |                                      +------------+\n" +
        "                                                                   |              |                      |                |               |        | <---+            |                                      |                                 +---+--------+                 |                                      |            |\n" +
        "                                                             +-----+---+          |                      +--------+       +-+             +--+     +----- ------------+--+                                   |                                     |        |                 |                                 +--> |  New       | <--------------+\n" +
        "                                                             |         |          |                               |         |                |                           |                                   |                                     |        +-----------+-----+                                 |    |  Guinea    | <----+         |\n" +
        "                                                             |         +----------+                               |         |                |          Egypt            +---------------+                   |                                     |                    |                                       |    |            |      |         |\n" +
        "                                                             |         |                                          |         |                |                           |               |                   |                India                |   Southeast Asia   |                +-----------------+    |    |            |      |         |\n" +
        "                                                             |         |                    Brazil                | <-----> |                |                           |               |                   |                                     |                    |                |                 | <--+    +------------+      |         |\n" +
        "                                                             |         |                                         ++         |                +---------------------+-----+------+        +-------+           |                                     |                    |                |                 |                             |         |\n" +
        "                                                             |         |                                         |          |                                      |            |                |           |                                 +---+---------+          |        +-------+                 |                             |         |\n" +
        "                                                             |         +--------+                         +------+          |       North Africa                   |            +--------+       |           +--------+                        |             |          |        |                   +-----+    +------------------------+         |\n" +
        "                                                             |                  |                         |                 |                                      |                     |   +-> |           |        |                        |             |          |        |                   |          |                                  |\n" +
        "                                                             |       Peru       |                         |                 |                               +------+                     |   |   +-----------+        |                        |             |          | <----> |       Indonesia   |          |                                  |\n" +
        "                                                             |                  +---------+               |                 +----------+                    |      |   East Africa       | <-+                        |                   +----+             |          |        |                   | <----+   |      ------+-------------+       |\n" +
        "                                                             |                            |               |                            |                    |      |                     | <-----------+              +----+              |                  +----------+        |       +-----------+      |   |      |     |             |       |\n" +
        "                                                             +-------+                    |               |                            +-----+--------------+      +--+                  |             |                   |              |                                      |       |                  |   +----> |     |             |  <----+\n" +
        "                                                                     |                    |               |                                  |                        |             -+---+             |                   |              |                                      +-------+                  |          |     |             |\n" +
        "                                                                     +-------+            +---------------+                                  |      Congo             |              |                 |                   |            +-+                                                                 +--------> |     |             |\n" +
        "                                                                     |       |                            |                                  |                        |              |                 |                   +----+       |                                                                              |     |             |\n" +
        "                                                                     |       |                            |                                  |                        |              |                 |                        |       |                                                         +--------------------+    ++             +--------+\n" +
        "                                                                     |       +------------+---------------+                                  +-----+-------+          +---+    +-----+                 |                        +--+    |                                                         |                         |                       |\n" +
        "                                                                     |                    |                                                        |       |              |    |                       |                           |    |                                                         |                         |                       |\n" +
        "                                                                     |                    |                                                        |       |              |    |           +-----+     |                           +----+                                                         |                         |      Eastern          |\n" +
        "                                                                     +-----+   Argentina  |                                                        |       +----+         |    |           |     | <---+                                                                                          |                         |      Australia        |\n" +
        "                                                                           |              |                                                        |            +---------+---++           |     |                                                                                                |                         |                       |\n" +
        "                                                                           |              |                                                        |                          |      +-----+     +----+                                                                                           |                         |                       |\n" +
        "                                                                           |              |                                                        |                          |      |                |                                                                                           |        Western          |                       |\n" +
        "                                                                           |              |                                                        |          South Africa    | <--> |  Madagascar    |                                                                                      +----+        Australia        +-----+                 +---+\n" +
        "                                                                           +----+         |                                                        |                          |      |                |                                                                                      |                                    |                     |\n" +
        "                                                                                |         |                                                        +----+                     |      +----------------+                                                                                      |                                    |                     |\n" +
        "                                                                                |         |                                                             |             +-------+                                                                                                              |                                    |                     |\n" +
        "                                                                                +----+    |                                                             |             |                                                                                                                      |                                    |                     |\n" +
        "                                                                                     |    +-----+                                                       |             |                                                                                                                      |                                    |                 ----+\n" +
        "                                                                                     |          |                                                       +-----+       |                                                                                                                      |                                    |                 |\n" +
        "                                                                                     +----------+                                                             |       |                                                                                                                      +---+               +----------+-----+                 |\n" +
        "                                                                                                                                                              +-------+                                                                                                                          |               |          |                       |\n" +
        "                                                                                                                                                                                                                                                                                                 +---------------+          |                       |\n" +
        "                                                                                                                                                                                                                                                                                                                            |                       |\n" +
        "                                                                                                                                                                                                                                                                                                                            +-----------------------+\n",map.countries.get(0).getOwner().getName()+"("+map.countries.get(0).getTroops()+")");
    }
}
