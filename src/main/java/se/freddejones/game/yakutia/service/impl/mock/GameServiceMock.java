package se.freddejones.game.yakutia.service.impl.mock;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;
import se.freddejones.game.yakutia.exception.NotEnoughPlayersException;
import se.freddejones.game.yakutia.exception.OnlyCreatorCanStartGame;
import se.freddejones.game.yakutia.exception.TooManyPlayersException;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameInviteDTO;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.service.GameService;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("mock")
public class GameServiceMock implements GameService {

    @Override
    public Long createNewGame(CreateGameDTO createGameDTO) throws NotEnoughPlayersException {
        return null;
    }

    @Override
    public List<GameDTO> getGamesForPlayerById(Long playerid) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setCanStartGame(true);
        gameDTO.setName("Du kan starta detta");
        gameDTO.setDate("201123123");
        gameDTO.setId(1L);
        gameDTO.setStatus(GameStatus.CREATED.toString());

        GameDTO gameDTO1 = new GameDTO();
        gameDTO1.setCanStartGame(false);
        gameDTO1.setDate("20230203");
        gameDTO1.setName("sdf");
        gameDTO1.setId(2L);
        gameDTO1.setStatus(GamePlayerStatus.INVITED.toString());

        List<GameDTO> games = new ArrayList<>();
        games.add(gameDTO);
        games.add(gameDTO1);
        return games;
    }

    @Override
    public void setGameToStarted(Long gameId, Long playerId) throws NotEnoughPlayersException, TooManyPlayersException, CouldNotCreateGameException, OnlyCreatorCanStartGame {

    }

    @Override
    public void setGameToFinished(Long gameId) {

    }

    @Override
    public void acceptGameInvite(GameInviteDTO gameInviteDTO) {

    }

    @Override
    public void declineGameInvite(GameInviteDTO gameInviteDTO) {

    }
}
