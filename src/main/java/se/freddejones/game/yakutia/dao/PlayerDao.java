package se.freddejones.game.yakutia.dao;


import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.PlayerId;

import java.util.List;

public interface PlayerDao {

    public PlayerId createPlayer(Player p) throws PlayerAlreadyExistsException;
    public List<Player> getAllPlayers();
    public Player getPlayerById(PlayerId playerId);
    public Player getPlayerByEmail(String email);
    public PlayerId updatePlayerName(String name, PlayerId playerId);
    public void mergePlayer(Player p);

}
