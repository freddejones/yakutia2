package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;

import java.util.List;

@Repository
public class PlayerDaoImpl extends AbstractDaoImpl implements PlayerDao {

    @Override
    public Long createPlayer(Player p) throws PlayerAlreadyExistsException {
        try {
            getCurrentSession().saveOrUpdate(p);
        } catch (ConstraintViolationException cve) {
            throw new PlayerAlreadyExistsException("Player with email: " + p.getEmail() + " already exists");
        }
        getCurrentSession().refresh(p);
        return p.getPlayerId();
    }

    @Override
    public List<Player> getAllPlayers() {
        return (List<Player>) getCurrentSession().getNamedQuery("Player.getAllPlayers").list();
    }

    @Override
    public Player getPlayerById(Long playerId) {
        return (Player) getCurrentSession().get(Player.class, playerId);
    }

    @Override
    public Player getPlayerByEmail(String email) {
        getCurrentSession().flush();
        Player p = (Player) getCurrentSession().getNamedQuery("Player.getPlayerByEmail").setParameter("email", email).uniqueResult();
        return p;
    }

    @Override
    public Long updatePlayerName(Player player) {
        getCurrentSession().saveOrUpdate(player);
        getCurrentSession().flush();
        return player.getPlayerId();
    }

}
