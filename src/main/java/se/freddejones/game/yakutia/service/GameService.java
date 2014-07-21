package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.PlayerId;

import java.util.List;

public interface GameService {

    public GameId createNewGame(CreateGame createGame);
    public List<Game> getGamesForPlayerById(PlayerId playerId);
    public void startGame(GamePlayerId gamePlayerId);
    public void endGame(GameId gameId);
}
