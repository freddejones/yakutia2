package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;
import se.freddejones.game.yakutia.exception.NotEnoughPlayersException;
import se.freddejones.game.yakutia.exception.TooManyPlayersException;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameInviteDTO;

import java.util.List;

public interface GameService {

    public Long createNewGame(CreateGameDTO createGameDTO);
    public List<GameDTO> getGamesForPlayerById(Long playerid);
    public void setGameToStarted(Long gameId) throws NotEnoughPlayersException, TooManyPlayersException, CouldNotCreateGameException;
    public void setGameToFinished(Long gameId);
    public void acceptGameInvite(GameInviteDTO gameInviteDTO);
    public void declineGameInvite(GameInviteDTO gameInviteDTO);
}
