package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.translators.CreateGameBinder;
import se.freddejones.game.yakutia.model.translators.GetGamesBinder;
import se.freddejones.game.yakutia.service.GamePlayerStatusHandler;
import se.freddejones.game.yakutia.service.GameService;

import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;
    private final GamePlayerStatusHandler gamePlayerStatusHandler;
    private final CreateGameBinder createGameBinder;
    private final GetGamesBinder getGamesBinder;

    @Autowired
    public GameController(GameService gameService, GamePlayerStatusHandler gamePlayerStatusHandler) {
        this.gameService = gameService;
        this.gamePlayerStatusHandler = gamePlayerStatusHandler;
        this.createGameBinder = new CreateGameBinder();
        this.getGamesBinder = new GetGamesBinder();
    }

    @RequestMapping(value  = "/game/create", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    @ResponseBody
    public Long createNewGame(@RequestBody final CreateGameDTO createGameDTO) {
        CreateGame createGame = createGameBinder.bind(createGameDTO);
        return gameService.createNewGame(createGame).getGameId();
    }

    @RequestMapping(value = "/game/player/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<GameDTO> getAllGamesById(@PathVariable("playerId") Long id) {
        PlayerId playerId = new PlayerId(id);
        List<Game> games = gameService.getGamesForPlayerById(playerId);
        return getGamesBinder.bind(games, playerId);
    }

    @RequestMapping(value = "/game/start/{gamePlayerId}", method = RequestMethod.PUT)
    @ResponseBody
    public void startGame(@PathVariable("gamePlayerId") final Long gamePlayerId) throws Exception {
        gameService.startGame(new GamePlayerId(gamePlayerId));
    }

    @RequestMapping(value = "/game/accept/{gamePlayerId}", method = RequestMethod.POST)
    @ResponseBody
    public void acceptGameInvite(@PathVariable("gamePlayerId") final Long gamePlayerId) {
        gamePlayerStatusHandler.acceptGameInvite(new GamePlayerId(gamePlayerId));
    }

    @RequestMapping(value = "/game/decline/{gamePlayerId}", method = RequestMethod.POST)
    @ResponseBody
    public void declineGameInvite(@PathVariable("gamePlayerId") final Long gamePlayerId) {
        gamePlayerStatusHandler.declineGameInvite(new GamePlayerId(gamePlayerId));
    }
}
