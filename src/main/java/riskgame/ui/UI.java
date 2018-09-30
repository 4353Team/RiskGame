package riskgame.ui;

import riskgame.GameEngine;

/**
 * essentially an Observer (design pattern)
 */
public interface UI {
    public void addGame(GameEngine gameEngine);
    public void update();
}
