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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameId)) return false;

        GameId gameId1 = (GameId) o;

        if (gameId != null ? !gameId.equals(gameId1.gameId) : gameId1.gameId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return gameId != null ? gameId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GameId{" +
                "gameId=" + gameId +
                '}';
    }
}
