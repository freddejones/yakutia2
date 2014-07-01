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
    public String toString() {
        return "GamePlayerId{" +
                "gamePlayerId=" + gamePlayerId +
                '}';
    }
}
