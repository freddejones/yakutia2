package se.freddejones.game.yakutia.model.dto;

import se.freddejones.game.yakutia.entity.Player;

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

    public static Player bind(PlayerDTO playerDTO) {
        Player p = new Player();
        p.setName(playerDTO.getPlayerName());
        p.setEmail(playerDTO.getEmail());
        p.setPlayerId(playerDTO.getPlayerId());
        return p;
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
