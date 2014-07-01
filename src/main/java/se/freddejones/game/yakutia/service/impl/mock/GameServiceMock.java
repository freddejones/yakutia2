package se.freddejones.game.yakutia.service.impl.mock;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.service.GameService;
import java.util.List;

@Service
@Profile("mock")
public class GameServiceMock implements GameService {
    @Override
    public GameId createNewGame(CreateGameDTO createGameDTO) {
        return null;
    }

    @Override
    public List<Game> getGamesForPlayerById(PlayerId playerId) {
        return null;
    }

    @Override
    public void setGameToStarted(GamePlayerId gamePlayerId) {

    }

    @Override
    public void setGameToFinished(GameId gameId) {

    }

    @Override
    public void acceptGameInvite(GamePlayerId gamePlayerId) {

    }

    @Override
    public void declineGameInvite(GamePlayerId gamePlayerId) {

    }

//    @Override
//    public Long createNewGame(CreateGameDTO createGameDTO) throws NotEnoughPlayersException {
//        return null;
//    }
//
//    @Override
//    public List<GameDTO> getGamesForPlayerById(Long playerId) {
//        GameDTO gameDTO = new GameDTO();
//        gameDTO.setCanStartGame(true);
//        gameDTO.setName("Du kan starta detta");
//        gameDTO.setDate("201123123");
//        gameDTO.setId(1L);
//        gameDTO.setStatus(GameStatus.CREATED.toString());
//
//        GameDTO gameDTO1 = new GameDTO();
//        gameDTO1.setCanStartGame(false);
//        gameDTO1.setDate("20230203");
//        gameDTO1.setName("sdf");
//        gameDTO1.setId(2L);
//        gameDTO1.setStatus(GamePlayerStatus.INVITED.toString());
//
//        List<GameDTO> games = new ArrayList<>();
//        games.add(gameDTO);
//        games.add(gameDTO1);
//        return games;
//    }


}
