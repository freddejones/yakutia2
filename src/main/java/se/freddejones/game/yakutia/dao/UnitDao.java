package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.UnitId;

import java.util.List;

public interface UnitDao {

    List<Unit> getUnitsByGamePlayerId(GamePlayerId gamePlaerId);
    void setGamePlayerIdForUnit(GamePlayerId gamePlayerId, UnitId unitId);

}
