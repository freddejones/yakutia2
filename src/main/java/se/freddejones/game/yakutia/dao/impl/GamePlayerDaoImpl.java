package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;

import java.util.List;

@Repository
public class GamePlayerDaoImpl extends AbstractDaoImpl implements GamePlayerDao {

    @Override
    public Session getSession() {
        return getCurrentSession();
    }

    @Override
    public List<GamePlayer> getGamePlayersByPlayerId(Long playerId) {
        return (List<GamePlayer>) getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersFromPlayerId")
                .setParameter("playerId", playerId).list();
    }

    @Override
    public List<GamePlayer> getGamePlayersByGameId(Long gameId) {
        return (List<GamePlayer>) getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersForGameId")
                .setParameter("gameId", gameId).list();
    }

    @Override
    public GamePlayer getGamePlayerByGameIdAndPlayerId(Long playerId, Long gameId) {
        return (GamePlayer) getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersFromPlayerIdAndGameId")
                .setParameter("playerId", playerId)
                .setParameter("gameId", gameId).uniqueResult();
    }

    @Override
    public GamePlayer getGamePlayerByGamePlayerId(Long gamePlayerId) {
        return (GamePlayer) getCurrentSession().get(GamePlayer.class, gamePlayerId);
    }

    @Override
    public GamePlayer getGamePlayerByGameIdAndTerritory(Long gameId, Territory territory) {
        List<GamePlayer> gamePlayers = getGamePlayersByGameId(gameId);
        for (GamePlayer gamePlayer : gamePlayers) {
            for (Unit unit : gamePlayer.getUnits()) {
                if (unit.getTerritory() == territory) {
                    return gamePlayer;
                }
            }
        }
        return null;
    }

    @Override
    public void setUnitsToGamePlayer(Long gamePlayerId, Unit unit) {
        Session session = getCurrentSession();
        GamePlayer gamePlayer = (GamePlayer) session.get(GamePlayer.class, gamePlayerId);
        unit.setGamePlayer(gamePlayer);
        session.saveOrUpdate(unit);
    }

    @Override
    public Unit getUnassignedLand(Long gamePlayerId) {
        GamePlayer gamePlayer = (GamePlayer) getCurrentSession().get(GamePlayer.class, gamePlayerId);
        for (Unit unit : gamePlayer.getUnits()) {
            if (unit.getTerritory() == Territory.UNASSIGNEDLAND) {
                return unit;
            }
        }
        return new Unit();
    }

    @Override
    public void setActionStatus(Long gamePlayerId, ActionStatus actionStatus) {
        GamePlayer gamePlayer = (GamePlayer) getCurrentSession().get(GamePlayer.class, gamePlayerId);
        gamePlayer.setActionStatus(actionStatus);
    }

    @Override
    public void updateGamePlayer(GamePlayer gamePlayer) {
        getCurrentSession().saveOrUpdate(gamePlayer);
    }


}
