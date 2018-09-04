package com.company;
import java.util.*;

public class Game {
    private int maxPlayers = 6;
    private int minPlayers = 2;
    private int initialInfantryCount = 0;
    public Map map;
    public List<Player> players;
    public Dice gameDice;
    private Stack<Card> cardStack;
    public List<Card> cards;
    private int turn = 0;
    private int phase = 0;
    private int numberOfPlayers = 0;
    private int setsOfRiskCardsTraded = 0;
    public enum GamePhase {
        DRAFT, ATTACK, FORTIFY
    }
    // initialize map
    public Game() {
        //load map
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
        }catch (Exception e ) {};

        // create players for test purpose
        players = new ArrayList<Player>();
        players.add(new Player("Navin"));
        players.add(new Player("Sunada"));
        players.add(new Player("Jack"));


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

            System.out.print(p.getName() + " rolled " );
            p.rollDices(1);
            if (p.dice.get(0).getFaceValue() > highestPlayer.dice.get(0).getFaceValue())
                highestPlayer = p;
            for(Dice d: p.dice) {
                System.out.print(d.getFaceValue() + " ");
            }
            System.out.println();
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
            nextTurn();
        }
        for (int i = 0; i < initialInfantryCount - 42;i++) {
            Player curr = players.get(turn);
            List<Country> c = getTerritoriesOwnedBy(curr);
            Random rand = new Random();
            int a = rand.nextInt(c.size());
             c.get(a).addInfantry(curr);
            nextTurn();
        }

     // System.out.println("Randomly populating the map ... ");
        for(Player p: players) {
            System.out.println();
            System.out.println(p.getName());
            System.out.println("-------------------------------------------");
            printList(getTerritoriesOwnedBy(p));
            System.out.println(" - " + getNumTerritoriesOwnedBy(p) + " troops across " +getTerritoriesOwnedBy(p).size() + " countries ");
        }
        // console render
        System.out.println();
        System.out.println("---------------------------snapshot of the map-------------------------------------------------------------------------------------");

        System.out.println(map.toString());
        System.out.println(map.getTotalTroops() +" troops across 42 countries, 6 continents");


        for (Card c:cards) {
            System.out.println((c.getClass() == new SpecialCard().getClass())?"Special":"Not so special");
        }

        Scanner scan = new Scanner(System.in);
        //============ main game loop =================
        while (true) {

            //-----------------calculate bonus troops
            // draft new troops
                // get number of territories player occupies
                Player player = players.get(turn);
                System.out.println(player.getName() +"'s " + getGamePhase() + " phase");
                player.setTotalInitialTroops((int)Math.floor(getTerritoriesOwnedBy(player).size()/3.0));
                System.out.println(player.getName() + " controls " + getTerritoriesOwnedBy(player).size() + " territories, therefore receives " + player.getTotalInitialTroops() + " troops" );
             //   System.out.println(player.getTotalInitialTroops());
                // does this player control a continent , if so add respective value
                // check matched RISK cards from a set of 3 cards this player has accumulated

            //- draft phase






             nextPhase();
            // attack if you want by rolling dice
            System.out.println(player.getName() +"'s " + getGamePhase() + " phase");




            // fortify position
            nextPhase();
            System.out.println(player.getName() +"'s " + getGamePhase() + " phase");

            scan.nextLine();
            nextTurn();
            nextPhase();
        }

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
                 for (Country d : mc) {
                     System.out.println(d.getName()+ " : " +d.continent + " (troop/s: " + d.getTroops() +") " );
//                     for (Country c : d.getNeighbors()) {
//                         System.out.println(c.getName() + " | ");
//                     }
                 }


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

}
