package se.freddejones.game.yakutia.model;

public class GameId {

    private final Long gameId;

    public GameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }

    @Override
    public String toString() {
        return "GameId{" +
                "gameId=" + gameId +
                '}';
    }
}
