package se.freddejones.game.yakutia.model.translators;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;

public class PlayerMapper {

    public Player map(PlayerDTO playerDTO) {
        Player p = new Player();
        p.setName(playerDTO.getPlayerName());
        p.setEmail(playerDTO.getEmail());
        if (playerDTO.getPlayerId() != null) {
            p.setPlayerId(playerDTO.getPlayerId());
        }
        return p;
    }

    public PlayerDTO map(Player p) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setEmail(p.getEmail());
        playerDTO.setPlayerId(p.getPlayerId());
        playerDTO.setPlayerName(p.getName());
        return playerDTO;
    }
}
