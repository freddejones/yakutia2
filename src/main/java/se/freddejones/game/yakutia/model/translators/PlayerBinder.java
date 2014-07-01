package se.freddejones.game.yakutia.model.translators;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;

public class PlayerBinder {

    public Player bind(PlayerDTO playerDTO) {
        Player p = new Player();
        p.setName(playerDTO.getPlayerName());
        p.setEmail(playerDTO.getEmail());
        if (playerDTO.getPlayerId() != null) {
            p.setPlayerId(playerDTO.getPlayerId());
        }

        return p;
    }
}
