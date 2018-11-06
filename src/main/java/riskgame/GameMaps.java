package riskgame;

import riskgame.gameobject.Territory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameMaps {
    public static final String TERRITORY_STRAIGHT_LINE = "6 Territory Straight Line";
    public static final String CLASSIC_WORLD = "Classic World";
    public static final String SMALL_WORLD = "Small World";
    public static List<GameMap> mapList = new ArrayList<>();
    private static GameMaps instance = null;

    private GameMaps() {
        //building map1;
        GameMap map1 = new GameMap();
        map1.name = CLASSIC_WORLD;
        mapList.add(map1);

        //building map2;
        GameMap map2 = new GameMap();
        {
            map2.name = TERRITORY_STRAIGHT_LINE;
            Territory t1 = new Territory("LeftMost");
            Territory t2 = new Territory("Left");
            Territory t3 = new Territory("Middle");
            Territory t4 = new Territory("Right");
            Territory t5 = new Territory("RightMost");

            makeNeighbors(t1, t2);
            makeNeighbors(t2, t3);
            makeNeighbors(t3, t4);
            makeNeighbors(t4, t5);

            map2.territoryList.add(t1);
            map2.territoryList.add(t2);
            map2.territoryList.add(t3);
            map2.territoryList.add(t4);
            map2.territoryList.add(t5);
        }

        mapList.add(map2);

        GameMap map3 = new GameMap();
        {
            map3.name = SMALL_WORLD;
            Territory panama = new Territory("Panama");
            Territory colombia = new Territory("Colombia");
            Territory brazil = new Territory("Brazil");

            makeNeighbors(panama,colombia);
            makeNeighbors(colombia,brazil);

            map3.territoryList.add(panama);
            map3.territoryList.add(colombia);
            map3.territoryList.add(brazil);
        }
        mapList.add(map3);

    }

    private void makeNeighbors(Territory t1, Territory t2) {
        t1.addNeighbor(t2);
        t2.addNeighbor(t1);
    }

    public static GameMaps instanceOf() {
        if (instance == null) {
            instance = new GameMaps();
        }
        return instance;
    }

    public List<GameMap> getList() {
        return mapList;
    }

    public class GameMap {
        String name;
        List<Territory> territoryList = new ArrayList<>();

        public List<Territory> getConfiguredTerritories() {
            return new ArrayList<>(territoryList); // immutable
        }

        public String getName() {
            return name;
        }
    }
}
