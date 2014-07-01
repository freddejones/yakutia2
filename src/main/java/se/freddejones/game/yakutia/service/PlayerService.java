package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.PlayerId;

import java.util.List;


public interface PlayerService {

    public PlayerId createNewPlayer(Player p);
    public List<Player> getAllPlayers();
    public Player getPlayerById(PlayerId playerId);
    public boolean isPlayerFullyCreated(PlayerId id);
    public PlayerId updatePlayerName(PlayerId playerId, String name);
    public Player getPlayerByEmail(String email);

}
