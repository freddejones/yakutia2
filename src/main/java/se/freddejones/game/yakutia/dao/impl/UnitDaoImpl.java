package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.UnitId;

import java.util.List;

@Repository("unitDao")
public class UnitDaoImpl implements UnitDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UnitDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Unit> getUnitsByGamePlayerId(GamePlayerId gamePlayerId) {
        return (List) sessionFactory.getCurrentSession().getNamedQuery("Unit.getUnitsByGamePlayer").setParameter("gpid", gamePlayerId.getGamePlayerId()).list();
    }

    @Override
    public void setGamePlayerIdForUnit(GamePlayerId gamePlayerId, UnitId unitId) {
        Unit unit = (Unit) sessionFactory.getCurrentSession().get(Unit.class, unitId.getUnitId());
        GamePlayer gamePlayer = getGamePlayerFromGamePlayerId(gamePlayerId);
        unit.setGamePlayer(gamePlayer);
    }

    @Override
    public List<Unit> getUnitsForGamePlayerIdAndTerritory(GamePlayerId gamePlayerId, Territory territory) {
        List<Unit> units = (List<Unit>) sessionFactory.getCurrentSession().getNamedQuery("Unit.getUnitsByGamePlayerAndTerritory")
                .setParameter("gpid", gamePlayerId.getGamePlayerId())
                .setParameter("territory", territory).list();
        return units;
    }

    private GamePlayer getGamePlayerFromGamePlayerId(GamePlayerId gamePlayerId) {
        Unit u = (Unit) sessionFactory.getCurrentSession().getNamedQuery("Unit.getUnitsByGamePlayer").setParameter("gpid", gamePlayerId.getGamePlayerId()).uniqueResult();
        return u.getGamePlayer();
    }

}
