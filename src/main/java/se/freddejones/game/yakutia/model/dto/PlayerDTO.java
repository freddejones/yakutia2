package se.freddejones.game.yakutia.model.dto;

import java.io.Serializable;

public class PlayerDTO implements Serializable {

    private String playerName;
    private String email;
    private Long playerId;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "playerName='" + playerName + '\'' +
                ", email='" + email + '\'' +
                ", playerId=" + playerId +
                '}';
    }
}
