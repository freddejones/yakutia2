package se.freddejones.game.yakutia.model.dto;

public class GameStateModelDTO {

    private String state;
    private Long gamePlayerId;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(Long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }
}
