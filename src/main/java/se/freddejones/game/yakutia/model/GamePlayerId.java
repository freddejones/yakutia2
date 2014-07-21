package se.freddejones.game.yakutia.model;

public class GamePlayerId {

    private final Long gamePlayerId;

    public GamePlayerId(Long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public Long getGamePlayerId() {
        return gamePlayerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamePlayerId)) return false;

        GamePlayerId that = (GamePlayerId) o;

        return gamePlayerId.equals(that.gamePlayerId);

    }

    @Override
    public int hashCode() {
        return gamePlayerId.hashCode();
    }

    @Override
    public String toString() {
        return "GamePlayerId{" +
                "gamePlayerId=" + gamePlayerId +
                '}';
    }
}
