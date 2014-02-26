package se.freddejones.game.yakutia.model.statuses;

public enum GameStatus {

    CREATED ("CREATED"),
    ONGOING ("ONGOING"),
    FINISHED ("FINISHED");

    private String gameStatus;

    private GameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    @Override
    public String toString() {
        return gameStatus;
    }

}
