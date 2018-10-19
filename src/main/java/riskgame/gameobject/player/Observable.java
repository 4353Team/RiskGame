package riskgame.gameobject.player;

public interface Observable {
    public void register(Observer inObserver);
    public void unregister(Observer inObserver);
    public void notifyObserver(Observer inObserver);
}
