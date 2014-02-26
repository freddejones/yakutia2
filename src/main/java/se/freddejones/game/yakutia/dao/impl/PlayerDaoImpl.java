package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;

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

}
