package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;

import java.util.List;

/**
 * User: Fredde
 * Date: 12/9/13 11:09 PM
 */
public interface GamePlayerDao {

    List<GamePlayer> getGamePlayersByPlayerId(PlayerId playerId);
    List<GamePlayer> getGamePlayersByGameId(GameId gameId);
    GamePlayer getGamePlayerByGameIdAndPlayerId(PlayerId playerId, GameId gameId);
    GamePlayer getGamePlayerByGamePlayerId(GamePlayerId gamePlayerId);
    void setUnitsToGamePlayer(GamePlayerId gamePlayerId, Unit unit);
    Unit getUnassignedLand(GamePlayerId gamePlayerId);
    void setActionStatus(GamePlayerId gamePlayerId, ActionStatus actionStatus);
    void updateGamePlayer(GamePlayer gamePlayer);
}
