package com.company;
import com.sun.deploy.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game  {
    private int maxPlayers = 6;
    private int minPlayers = 2;
    private int initialInfantryCount = 0;
    public Map map;
    public List<Player> players;
    public Dice gameDice;
    private Stack<Card> cardStack;
    public List<Card> cards;
    private int turn = 0;
    private static int totalTurnsCounter = 0;
    private int phase = 0;
    private int numberOfPlayers = 0;
    private int setsOfRiskCardsTraded = 0;
    public enum GamePhase {
        DRAFT, ATTACK, FORTIFY
    }


    // initialize map
    public Game() {
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



        //load map
        map = loadMap();


        // create players for test purpose
        players = new ArrayList<Player>();
        players.add(new Player("Navin"));
        players.add(new Player("Sunada"));
        players.add(new Player("Jack"));
        players.add(new Player("John"));
        players.add(new Player("Jim"));
        players.add(new Player("Mark"));

        numberOfPlayers = players.size();

        initialInfantryCount = getInitialInfantryCount(numberOfPlayers) * numberOfPlayers;

        // calculate and assign number of infantry at the beginning
        for(Player p: players) {
            int a = getInitialInfantryCount(players.size());
            p.setTotalInitialTroops(a);
        }
        System.out.println("We have " + players.size() + " players!");

        System.out.println("Each players now rolling dice to determine who is going first.");

        // roll dice
        Player highestPlayer = players.get(0);
        for(Player p: players) {

            //System.out.print(p.getName() + " rolled " );
            System.out.printf("%s is rolling dice ...\n",p.getName());
            printDice(p.rollDices(1));
            if (p.dice.get(0).getFaceValue() > highestPlayer.dice.get(0).getFaceValue())
                highestPlayer = p;
//            for(Dice d: p.dice) {
//                System.out.print(d.getFaceValue() + " ");
//            }
//            System.out.println();
        }
        turn = players.indexOf(highestPlayer);
        System.out.println();
        System.out.println(highestPlayer.getName() + " is going first");


        ArrayList<Integer> randomCountries = new ArrayList<Integer>();
        for (int i= 0; i < map.countries.size(); i++) {
            randomCountries.add(i);

        }
        Collections.shuffle(randomCountries);

         for (int i= 0; i < 42; i++) {
            map.countries.get(randomCountries.get(i)).addInfantry(players.get(turn));
            players.get(turn).addTerritory(map.countries.get(randomCountries.get(i)));
            nextTurn();
        }
        for (int i = 0; i < initialInfantryCount - 42; i++) {
            Player curr = players.get(turn);
            List<Country> c = getTerritoriesOwnedBy(curr);
            Random rand = new Random();
            int a = rand.nextInt(c.size());
            c.get(a).addInfantry(curr);
            if (!(curr.getTerritories().contains(c.get(a))))
                curr.addTerritory(c.get(a));
            nextTurn();
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

        List<Boolean> noAttackMoves = new ArrayList<>();
        for (int i=0;i<players.size();i++){
            noAttackMoves.add(false);
        }
        Scanner scan = new Scanner(System.in);
        //============ main game loop =================
        while (true) {
//            boolean doWeHaveAWinner = true;
//
//            for (Country c :
//                    map.countries) {
//                if (c.getOwner()!=players.get(turn)) doWeHaveAWinner = false;
//            }
            if (players.size() == 1)
            {
                System.out.println(players.get(turn) + " wins!!!");
                System.exit(0);
            }


            if (getTerritoriesOwnedBy(players.get(turn)).isEmpty()) {
                players.remove(players.get(turn));
                System.out.println(players.get(turn).getName() + " eliminated!");
                numberOfPlayers--;
                nextTurn();
                continue;
            }
            boolean exitNow = true;
            for(int i =0;i<numberOfPlayers;i++){
                //System.out.println(noAttackMoves.get(i) + " for "+players.get(i).getName());
                if (noAttackMoves.get(i) == false)
                    exitNow = false;
            }
            if (exitNow) {
                System.out.println();
                System.out.println("Game is now at a stalemate!! That's what you want in a successful simulation. It took "+totalTurnsCounter+" turns for the game to achieve stalemate.");
                System.out.println();
                System.out.print("\u2714");
              //  render(map);
                break;
            }

            //-----------------calculate bonus troops
            // draft new troops
                // get number of territories player occupies
                Player player = players.get(turn);

                System.out.println();
                System.out.println("------------------------------");
                System.out.println(player.getName().toUpperCase()+"'s TURN ("+turn+")");
                System.out.println("------------------------------");
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
            players.get(turn).addToTotalInitialTroops(getContinentOccupationPoints(player));

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
                System.out.println("Countries owned by " + player.getName());
                printList(playerTerritory);


                // list of countries with more than 1 troop
                System.out.println();


                playerTerritoryThatCanAttack = getContriesPlayerCanAttackFrom(playerTerritory,player);
                if(!playerTerritoryThatCanAttack.isEmpty())
                    System.out.printf("%s can attack from\n",player.getName());

                if (playerTerritoryThatCanAttack.isEmpty()) {
                    noAttackMoves.set(turn,true);
                    nextTurn();
                    continue;
                }
                printList(playerTerritoryThatCanAttack);
                printList(playerTerritoryThatCanAttack);
                System.out.println();
                Country attackingCountry = playerTerritoryThatCanAttack.get(rand.nextInt(playerTerritoryThatCanAttack.size()));
 //               System.out.println("Attacking Country picked: "+attackingCountry.getName());



                // randomly pick a country from player's territory (attackingCountry)
                List<Country> neighboringCountryPlayerCanAttackTo = new ArrayList<>();


                neighboringCountryPlayerCanAttackTo = getNeighboringCountryPlayerCanAttackTo(attackingCountry,player);
                System.out.println();
                if (neighboringCountryPlayerCanAttackTo.isEmpty()) {
                    playerTerritoryThatCanAttack.remove(attackingCountry);
                    System.out.println("Removing " + attackingCountry.getName());
                    continue;
                }


     //           System.out.println("All neighboring countries of "+attackingCountry.getName());
     //           printList(attackingCountry.getNeighbors());
     //           System.out.println();
                System.out.printf("%s can attack \n",attackingCountry.getName());
                printList(neighboringCountryPlayerCanAttackTo);
                System.out.println();
                Country defendingCountry = neighboringCountryPlayerCanAttackTo.get(rand.nextInt(neighboringCountryPlayerCanAttackTo.size()));
  //              System.out.println("Defending country " + defendingCountry.getName());
                System.out.printf("(%d)%s (%d troops) attacking (%d)%s (%d troops)\n",map.countries.indexOf(attackingCountry),attackingCountry.getName(),attackingCountry.getTroops(),map.countries.indexOf(defendingCountry),defendingCountry.getName(),defendingCountry.getTroops());
                attack(attackingCountry,defendingCountry);

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

//            for (int i =0; i<numberOfPlayers;i++)
//                nextTurn();
            nextTurn();
            nextPhase();
        }

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
            country.addInfantry(p);
        }
    }

     private void attack(Country origin, Country destination) {
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
         System.out.println("Comparing " + ik + " dice.");
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
            if (countries.containsAll(player.getTerritories()))
                tp += points.get(continent);
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
     private Player nextTurn() {
        totalTurnsCounter++;
        turn++;
        if (turn >= numberOfPlayers)
            turn = 0;
        return players.get(turn);
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

    public Map loadMap() {
        map = new Map();
        cards = new ArrayList<Card>();
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
        Card c43 = new SpecialCard();
        Card c44 = new SpecialCard();

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
        cards.add(c43);
        cards.add(c44);
        Collections.shuffle(cards);
        try {
            cardStack.addAll(cards);
        }catch (Exception e ) {}
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
