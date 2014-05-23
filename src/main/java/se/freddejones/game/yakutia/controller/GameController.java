package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.NoGameFoundException;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.*;
import se.freddejones.game.yakutia.service.GameService;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/game")
public class GameController {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value  = "/create", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    @ResponseBody
    public Long createNewGame(@RequestBody final CreateGameDTO createGameDTO) {
        LOGGER.info("Received CreateGameDTO: " + createGameDTO.toString());
        return gameService.createNewGame(createGameDTO);
    }

    @RequestMapping(value = "/get/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<GameDTO> getAllGamesById(@PathVariable("playerId") Long playerid) {
        LOGGER.info("Getting games for playerId: " + playerid);
        List<GameDTO> list = gameService.getGamesForPlayerById(playerid);
        LOGGER.info("Fetched " + list.size() + " number of games");
        return list;
    }

    @RequestMapping(value = "/start/{gameId}", method = RequestMethod.PUT)
    @ResponseBody
    public void startGame(@PathVariable("gameId") Long gameId) throws Exception {
        LOGGER.info("Starting game for gameId: " + gameId);
        gameService.setGameToStarted(gameId);
    }

    @RequestMapping(value = "/accept", method = RequestMethod.PUT)
    @ResponseBody
    public void acceptGameInvite(@RequestBody final GameInviteDTO gameInviteDTO) {
        LOGGER.info("PlayerId " + gameInviteDTO.getPlayerId() + " accepts game " + gameInviteDTO.getGameId());
        gameService.acceptGameInvite(gameInviteDTO);
    }

    @RequestMapping(value = "/decline", method = RequestMethod.PUT)
    @ResponseBody
    public void declineGameInvite(@RequestBody final GameInviteDTO gameInviteDTO) {
        LOGGER.info("PlayerId " + gameInviteDTO.getPlayerId() + " declines game " + gameInviteDTO.getGameId());
        gameService.declineGameInvite(gameInviteDTO);
    }
}
