package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.exception.CannotCreateGameException;
import se.freddejones.game.yakutia.exception.CannotStartGameException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.service.GameService;
import se.freddejones.game.yakutia.service.GameSetupService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service("gameservice")
//@Profile("default")
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private static final Logger LOG = Logger.getLogger(GameServiceImpl.class.getName());

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
    public GameId createNewGame(CreateGame createGame) {
        if (createGame.getInvitedPlayers().size() < 2 ||
                createGame.getInvitedPlayers().size() >= GameTerritoryHandlerServiceImpl.getLandAreas().size()) {
            throw new CannotCreateGameException("Either too few or too many players invited");
        }
        return gameDao.createNewGame(createGame);
    }

    @Override
    public List<Game> getGamesForPlayerById(PlayerId playerId) {
        List<Game> games = new ArrayList<>();
        List<GamePlayer> gamePlayersList = gamePlayerDao.getGamePlayersByPlayerId(playerId);
        for(GamePlayer gamePlayer : gamePlayersList) {
            games.add(gamePlayer.getGame());
        }
        return games;
    }

    @Override
    @Transactional(readOnly = false)
    public void startGame(GamePlayerId gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        Game game = gamePlayer.getGame();
        validateGameToStart(gamePlayer);
        gameSetupService.initializeNewGame(game.getPlayers());
        gameDao.startGame(new GameId(game.getGameId()));
    }

    @Override
    @Transactional(readOnly = false)
    public void endGame(GameId gameId) {
        gameDao.endGame(gameId);
    }

    private void validateGameToStart(GamePlayer gamePlayer) {
        Game game = gamePlayer.getGame();
        if (!game.isCreatorOfGame(gamePlayer.getPlayerId())) {
            throw new CannotStartGameException("Only creator of the game can start the game");
        } else if (!checkIfEnoughInvitedPlayers(new GameId(game.getGameId()))) {
            throw new CannotStartGameException("Not enough players to create game");
        }

        // TODO check number too high of players
    }

    private boolean checkIfEnoughInvitedPlayers(GameId gameId) {
        int count = 0;
        for (GamePlayer gamePlayer : gamePlayerDao.getGamePlayersByGameId(gameId)) {
            if (gamePlayer.getGamePlayerStatus() == GamePlayerStatus.ACCEPTED) {
                count++;
            }

            if (count >= 2) {
                return true;
            }
        }
        return false;
    }
}
