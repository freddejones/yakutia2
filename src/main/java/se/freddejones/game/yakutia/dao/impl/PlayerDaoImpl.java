package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;

import java.util.List;
import java.util.logging.Logger;

@Repository
public class PlayerDaoImpl extends AbstractDaoImpl implements PlayerDao {

    private static final Logger LOGGER = Logger.getLogger(PlayerDaoImpl.class.getName());
    public static final String QUERY_PLAYER_GET_ALL_PLAYERS = "Player.getAllPlayers";
    public static final String QUERY_PLAYER_GET_PLAYER_BY_EMAIL = "Player.getPlayerByEmail";
    public static final String EMAIL_PARAMETER = "email";

    @Override
    public Long createPlayer(Player p) throws PlayerAlreadyExistsException {
        try {
            getCurrentSession().saveOrUpdate(p);
        } catch (ConstraintViolationException cve) {
            LOGGER.warning(cve.getMessage());
            throw new PlayerAlreadyExistsException("Player with email: " + p.getEmail() + " already exists");
        }
        getCurrentSession().refresh(p);
        return p.getPlayerId();
    }

    @Override
    public List<Player> getAllPlayers() {
        return (List<Player>) getCurrentSession().getNamedQuery(QUERY_PLAYER_GET_ALL_PLAYERS).list();
    }

    @Override
    public Player getPlayerById(Long playerId) {
        return (Player) getCurrentSession().get(Player.class, playerId);
    }

    @Override
    public Player getPlayerByEmail(String email) {
        getCurrentSession().flush();
        Player p = (Player) getCurrentSession().getNamedQuery(QUERY_PLAYER_GET_PLAYER_BY_EMAIL).setParameter(EMAIL_PARAMETER, email).uniqueResult();
        return p;
    }

    @Override
    public Long updatePlayerName(Player player) {
        getCurrentSession().saveOrUpdate(player);
        getCurrentSession().flush();
        return player.getPlayerId();
    }

}
