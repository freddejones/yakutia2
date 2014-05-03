package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.List;
import java.util.logging.Logger;

@Service("playerservice")
public class PlayerServiceImpl implements PlayerService {

    private Logger log = Logger.getLogger(PlayerServiceImpl.class.getName());

    @Autowired
    PlayerDao playerDao;

    @Override
    @Transactional(readOnly = false)
    public Long createNewPlayer(Player p) throws PlayerAlreadyExistsException {
        return playerDao.createPlayer(p);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    @Override
    public Player getPlayerById(Long playerId) {
        return playerDao.getPlayerById(playerId);
    }


    @Override
    public boolean isPlayerFullyCreated(Long id) {
        Player p = playerDao.getPlayerById(id);
        if (p == null || p.getName() == null || p.getName().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = false)
    public Long updatePlayerName(Player p) {
        Player playerToUpdate = playerDao.getPlayerById(p.getPlayerId());
        playerToUpdate.setName(p.getName());
        return playerDao.updatePlayerName(playerToUpdate);
    }

    @Override
    public Player getPlayerByEmail(String email) {
        Player p = playerDao.getPlayerByEmail(email);
        return p;
    }


}
