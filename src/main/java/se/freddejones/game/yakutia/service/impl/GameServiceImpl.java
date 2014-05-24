package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;
import se.freddejones.game.yakutia.exception.NotEnoughPlayersException;
import se.freddejones.game.yakutia.exception.OnlyCreatorCanStartGame;
import se.freddejones.game.yakutia.exception.TooManyPlayersException;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameInviteDTO;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.service.GameService;
import se.freddejones.game.yakutia.service.GameSetupService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static se.freddejones.game.yakutia.model.GameManager.getLandAreas;

@Service("gameservice")
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = Logger.getLogger(GameServiceImpl.class.getName());

    private GameDao gameDao;
    private GamePlayerDao gamePlayerDao;
    private GameSetupService gameSetupService;

    @Autowired
    public GameServiceImpl(GameDao gameDao, GamePlayerDao gamePlayerDao, GameSetupService gameSetupService) {
        this.gameDao = gameDao;
        this.gamePlayerDao = gamePlayerDao;
        this.gameSetupService = gameSetupService;
    }

    @Override
    @Transactional(readOnly = false)
    public Long createNewGame(CreateGameDTO createGameDTO) throws NotEnoughPlayersException {
        if (createGameDTO.getInvites().size() < 2) {
            throw new NotEnoughPlayersException("Not enough players added to game");
        }
        return gameDao.createNewGame(createGameDTO);
    }

    @Override
    public List<GameDTO> getGamesForPlayerById(Long playerid) {
        List<GameDTO> gamesForPlayer = new ArrayList<>();
        List<GamePlayer> gamePlayersList = gamePlayerDao.getGamePlayersByPlayerId(playerid);
        for(GamePlayer gamePlayer : gamePlayersList) {
            Game game = gameDao.getGameByGameId(gamePlayer.getGameId());
            GameDTO gameDto = buildGameDTO(playerid, game);
            gamesForPlayer.add(gameDto);
        }
        return gamesForPlayer;
    }

    private GameDTO buildGameDTO(Long playerid, Game game) {
        GameDTO gameDto = new GameDTO();
        gameDto.setId(game.getGameId());
        gameDto.setCanStartGame(playerid == game.getGameCreatorPlayerId());
        gameDto.setName(game.getName());
        gameDto.setDate(game.getCreationTime().toString());
        gameDto.setStatus(game.getGameStatus().toString());
        return gameDto;
    }

    @Override
    @Transactional(readOnly = false)
    public void setGameToStarted(Long gameId, Long playerId) throws NotEnoughPlayersException,
            TooManyPlayersException, CouldNotCreateGameException, OnlyCreatorCanStartGame {

        Game game = gameDao.getGameByGameId(gameId);
        if (game.getGameCreatorPlayerId() != playerId) {
            throw new OnlyCreatorCanStartGame("only player who created game can start");
        }


        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);

        if (gamePlayers.isEmpty() || gamePlayers.size() <= 1
                || !isAtLeastTwoAcceptedGamePlayers(gamePlayers)) {
            throw new NotEnoughPlayersException("Not enough players to start game");
        } else if (gamePlayers.size() > getLandAreas().size()) {
            throw new TooManyPlayersException("To many players to start game");
        }

        gameSetupService.initializeNewGame(gamePlayers);
        gameDao.startGame(gameId);
    }

    @Override
    @Transactional(readOnly = false)
    public void setGameToFinished(Long gameId) {
        LOGGER.info("setting game <" + gameId + "> to finished");
        gameDao.endGame(gameId);
    }

    @Override
    @Transactional(readOnly = false)
    public void acceptGameInvite(GameInviteDTO gameInviteDTO) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(gameInviteDTO.getPlayerId(), gameInviteDTO.getGameId());
        gamePlayer.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        gamePlayerDao.updateGamePlayer(gamePlayer);
    }

    @Override
    public void declineGameInvite(GameInviteDTO gameInviteDTO) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(gameInviteDTO.getPlayerId(), gameInviteDTO.getGameId());
        gamePlayer.setGamePlayerStatus(GamePlayerStatus.DECLINED);
        gamePlayerDao.updateGamePlayer(gamePlayer);
    }

    private boolean isAtLeastTwoAcceptedGamePlayers(List<GamePlayer> gamePlayers) {
        int count = 0;
        for (GamePlayer gp : gamePlayers) {
            if (gp.getGamePlayerStatus() == GamePlayerStatus.ACCEPTED) {
                count++;
            }
        }
        return count >= 2;
    }
}
