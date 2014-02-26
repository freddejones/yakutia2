package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;

import java.util.List;

public interface PlayerService {

    public Long createNewPlayer(Player p) throws PlayerAlreadyExistsException;
    public List<Player> getAllPlayers();
    public Player getPlayerById(Long playerId);

}
