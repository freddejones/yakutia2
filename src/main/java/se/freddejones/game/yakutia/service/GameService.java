package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;

import java.util.List;

public interface GameService {

    public GameId createNewGame(CreateGameDTO createGameDTO);
    public List<Game> getGamesForPlayerById(PlayerId playerId);
    public void setGameToStarted(GamePlayerId gamePlayerId);
    public void setGameToFinished(GameId gameId);
    public void acceptGameInvite(GamePlayerId gamePlayerId);
    public void declineGameInvite(GamePlayerId gamePlayerId);
}
