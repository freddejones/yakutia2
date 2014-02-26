package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.model.Territory;

/**
 * Created by fidde on 2014-02-09.
 */
public interface UnitDao {

    Long getGamePlayerIdByLandAreaAndGameId(Long gameId, Territory territory);

}
