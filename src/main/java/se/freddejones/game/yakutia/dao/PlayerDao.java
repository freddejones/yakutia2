package se.freddejones.game.yakutia.dao;


import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;

import java.util.List;

public interface PlayerDao {

    public Long createPlayer(Player p) throws PlayerAlreadyExistsException;
    public List<Player> getAllPlayers();
    public Player getPlayerById(Long playerId);
    public Player getPlayerByEmail(String email);
    public Long updatePlayerName(Player p);

}
