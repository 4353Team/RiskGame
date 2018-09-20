package com.company;

import java.util.List;

public class Turn {
    private Player turnPlayer;
    private int totalTurnsCounter;
    private List<Player> players;

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(Player turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public GamePhase getGamePhase() {
        return turnPlayer.getPlayerPhase();
    }

    public void setGamePhase(GamePhase gamePhase) {
        turnPlayer.setPlayerPhase(gamePhase);
    }

    public Turn(List<Player> players){
        totalTurnsCounter = 0;
        this.players = players;
    }
    public Player nextTurn(Player currentPlayer){
        if (!players.isEmpty()) {
            Game.totalTurnsCounter++;
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
    public void nextGamePhase() {
        if (turnPlayer.getPlayerPhase() == GamePhase.DRAFT) {
            setGamePhase(GamePhase.ATTACK);
        } else if (turnPlayer.getPlayerPhase() == GamePhase.ATTACK) {
            setGamePhase(GamePhase.FORTIFY);
        } else {
            turnPlayer = nextTurn(turnPlayer);
            setGamePhase(GamePhase.DRAFT);
        }
    }

}
