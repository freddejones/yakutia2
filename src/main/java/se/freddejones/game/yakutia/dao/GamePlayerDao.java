package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;

import java.util.List;

/**
 * User: Fredde
 * Date: 12/9/13 11:09 PM
 */
public interface GamePlayerDao {

    List<GamePlayer> getGamePlayersByPlayerId(Long playerId);
    List<GamePlayer> getGamePlayersByGameId(Long gameId);
    GamePlayer getGamePlayerByGameIdAndPlayerId(Long playerId, Long gameId);
    GamePlayer getGamePlayerByGamePlayerId(Long gamePlayerId);
    GamePlayer getGamePlayerByGameIdAndTerritory(Long gameId, Territory territory);
    void setUnitsToGamePlayer(Long gamePlayerId, Unit unit);
    Unit getUnassignedLand(Long gamePlayerId);
    void setActionStatus(Long gamePlayerId, ActionStatus actionStatus);

}
