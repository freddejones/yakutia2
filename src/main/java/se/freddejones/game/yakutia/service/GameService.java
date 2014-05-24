package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;
import se.freddejones.game.yakutia.exception.NotEnoughPlayersException;
import se.freddejones.game.yakutia.exception.OnlyCreatorCanStartGame;
import se.freddejones.game.yakutia.exception.TooManyPlayersException;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameInviteDTO;

import java.util.List;

public interface GameService {

    public Long createNewGame(CreateGameDTO createGameDTO) throws NotEnoughPlayersException;
    public List<GameDTO> getGamesForPlayerById(Long playerid);
    public void setGameToStarted(Long gameId, Long playerId) throws NotEnoughPlayersException, TooManyPlayersException, CouldNotCreateGameException, OnlyCreatorCanStartGame;
    public void setGameToFinished(Long gameId);
    public void acceptGameInvite(GameInviteDTO gameInviteDTO);
    public void declineGameInvite(GameInviteDTO gameInviteDTO);
}
