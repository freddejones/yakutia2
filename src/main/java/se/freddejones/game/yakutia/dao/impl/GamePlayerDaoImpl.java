package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;

import java.util.List;

@Repository("gamePlayerDao")
public class GamePlayerDaoImpl implements GamePlayerDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public GamePlayerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<GamePlayer> getGamePlayersByPlayerId(PlayerId playerId) {
        return (List<GamePlayer>) sessionFactory.getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersFromPlayerId")
                .setParameter("playerId", playerId.getPlayerId()).list();
    }

    @Override
    public List<GamePlayer> getGamePlayersByGameId(GameId gameId) {
        return (List<GamePlayer>) sessionFactory.getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersForGameId")
                .setParameter("gameId", gameId.getGameId()).list();
    }

    @Override
    public GamePlayer getGamePlayerByGameIdAndPlayerId(PlayerId playerId, GameId gameId) {
        return (GamePlayer) sessionFactory.getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersFromPlayerIdAndGameId")
                .setParameter("playerId", playerId.getPlayerId())
                .setParameter("gameId", gameId.getGameId()).uniqueResult();
    }

    @Override
    public GamePlayer getGamePlayerByGamePlayerId(GamePlayerId gamePlayerId) {
        return (GamePlayer) sessionFactory.getCurrentSession().get(GamePlayer.class, gamePlayerId.getGamePlayerId());
    }

    @Override
    public void updateUnitsToGamePlayer(GamePlayerId gamePlayerId, Unit unit) {
        Session session = sessionFactory.getCurrentSession();
        GamePlayer gamePlayer = (GamePlayer) session.get(GamePlayer.class, gamePlayerId.getGamePlayerId());
        unit.setGamePlayer(gamePlayer);
        session.saveOrUpdate(unit);
    }

    @Override
    public void setActionStatus(GamePlayerId gamePlayerId, ActionStatus actionStatus) {
        GamePlayer gamePlayer = (GamePlayer) sessionFactory.getCurrentSession().get(GamePlayer.class, gamePlayerId.getGamePlayerId());
        gamePlayer.setActionStatus(actionStatus);
    }

    @Override
    public void updateGamePlayer(GamePlayer gamePlayer) {
        sessionFactory.getCurrentSession().saveOrUpdate(gamePlayer);
    }


}
