package se.freddejones.game.yakutia.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.PlayerId;

import java.util.List;
import java.util.logging.Logger;

@Repository
public class PlayerDaoImpl implements PlayerDao {

    private static final Logger LOG = Logger.getLogger(PlayerDaoImpl.class.getName());
    public static final String QUERY_PLAYER_GET_ALL_PLAYERS = "Player.getAllPlayers";
    public static final String QUERY_PLAYER_GET_PLAYER_BY_EMAIL = "Player.getPlayerByEmail";
    public static final String EMAIL_PARAMETER = "email";
    private final SessionFactory sessionFactory;

    @Autowired
    public PlayerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PlayerId createPlayer(Player p) throws PlayerAlreadyExistsException {
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(p);
        } catch (ConstraintViolationException cve) {
            LOG.warning(cve.getMessage());
            throw new PlayerAlreadyExistsException("Player with email: " + p.getEmail() + " already exists");
        }
        sessionFactory.getCurrentSession().refresh(p);
        return new PlayerId(p.getPlayerId());
    }

    @Override
    public List<Player> getAllPlayers() {
        return (List<Player>) sessionFactory.getCurrentSession().getNamedQuery(QUERY_PLAYER_GET_ALL_PLAYERS).list();
    }

    @Override
    public Player getPlayerById(PlayerId playerId) {
        return (Player) sessionFactory.getCurrentSession().get(Player.class, playerId.getPlayerId());
    }

    @Override
    public Player getPlayerByEmail(String email) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.flush();
        Player p = (Player) currentSession.getNamedQuery(QUERY_PLAYER_GET_PLAYER_BY_EMAIL).setParameter(EMAIL_PARAMETER, email).uniqueResult();
        return p;
    }

    @Override
    public PlayerId updatePlayerName(String name, PlayerId playerId) {
        Session session = sessionFactory.getCurrentSession();
        Player p = (Player) session.get(Player.class, playerId.getPlayerId());
        p.setName(name);
        session.saveOrUpdate(p);
        session.flush();
        return new PlayerId(p.getPlayerId());
    }

}
