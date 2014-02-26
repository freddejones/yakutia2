package se.freddejones.game.yakutia.dao.impl;

import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.Territory;

import java.util.List;

@Repository
public class UnitDaoImpl extends AbstractDaoImpl implements UnitDao {

    @Override
    public Long getGamePlayerIdByLandAreaAndGameId(Long gameId, Territory territory) {
        List<Unit> units = (List<Unit>) getCurrentSession()
                .getNamedQuery("Unit.getUnitsByLandArea")
                .setParameter("laName", territory)
                .list();

        for (Unit unit : units) {
            if (unit.isSameGameId(gameId)) {
                return unit.getGamePlayer().getGamePlayerId();
            }
        }

        return -1L;
    }
}
