package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;

/**
 * User: Fredde
 * Date: 12/5/13 11:26 PM
 */
public interface GameDao {

    public GameId createNewGame(CreateGame createGame);

    public Game getGameByGameId(GameId gameId);

    public void startGame(GameId gameId);

    public void endGame(GameId gameId);
}
