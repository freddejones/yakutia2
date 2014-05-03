package se.freddejones.game.yakutia.service;

import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;

import java.util.List;

@Transactional(readOnly = true)
public interface PlayerService {

    public Long createNewPlayer(Player p) throws PlayerAlreadyExistsException;
    public List<Player> getAllPlayers();
    public Player getPlayerById(Long playerId);
    public boolean isPlayerFullyCreated(Long id);
    public Long updatePlayerName(Player p);
    public Player getPlayerByEmail(String email);

}
