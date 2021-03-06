package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.List;
import java.util.logging.Logger;

@Service("playerservice")
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

    private static final Logger log = Logger.getLogger(PlayerServiceImpl.class.getName());

    private final PlayerDao playerDao;

    @Autowired
    public PlayerServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    @Transactional(readOnly = false)
    public PlayerId createNewPlayer(Player p) {
        return playerDao.createPlayer(p);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    @Override
    public boolean isPlayerFullyCreated(PlayerId id) {
        Player p = playerDao.getPlayerById(id);
        return !(p == null || p.getName() == null || p.getName().isEmpty());
    }

    @Override
    public Player getPlayerById(PlayerId playerId) {
        return playerDao.getPlayerById(playerId);
    }

    @Override
    public Player getPlayerByEmail(String email) {
        return playerDao.getPlayerByEmail(email);
    }

    @Override
    @Transactional(readOnly = false)
    public PlayerId updatePlayerName(PlayerId playerId, String name) {
        return playerDao.updatePlayerName(name, playerId);
    }




}
