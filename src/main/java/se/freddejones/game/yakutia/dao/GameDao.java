package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;

import java.util.List;

/**
 * User: Fredde
 * Date: 12/5/13 11:26 PM
 */
public interface GameDao {

    public Long createNewGame(CreateGameDTO createGameDTO);

    public Game getGameByGameId(long gameId);

    public void startGame(long gameId);

    public void endGame(long gameId);
}
