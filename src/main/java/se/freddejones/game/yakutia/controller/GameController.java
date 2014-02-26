package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.exception.NoGameFoundException;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.service.GameService;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/game")
public class GameController {

    private Logger log = Logger.getLogger(GameController.class.getName());
    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value  = "/create", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    @ResponseBody
    public Long createNewGame(@RequestBody final CreateGameDTO createGameDTO) {
        log.info("Received CreateGameDTO: " + createGameDTO.toString());
        return gameService.createNewGame(createGameDTO);
    }

    @RequestMapping(value = "/get/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<GameDTO> getAllGamesById(@PathVariable("playerId") Long playerid) {
        log.info("Getting games for playerId: " + playerid);
        List<GameDTO> list = gameService.getGamesForPlayerById(playerid);
        log.info("Fetched " + list.size() + " number of games");
        return list;
    }

    @RequestMapping(value = "/get/{playerId}/game/{gameId}", method = RequestMethod.GET)
    @ResponseBody
    public List<TerritoryDTO> getGame(@PathVariable("playerId") Long playerId,
                                       @PathVariable("gameId") Long gameId) {
        log.info("Getting game information for gameId: " + gameId + " and playerId: " + playerId);
        List<TerritoryDTO> territoryDTOs = gameService.getTerritoryInformationForActiveGame(playerId, gameId);
        if (territoryDTOs.isEmpty()) {
            throw new NoGameFoundException();
        }
        return territoryDTOs;
    }

    @RequestMapping(value = "/start/{gameId}", method = RequestMethod.PUT)
    @ResponseBody
    public void startGame(@PathVariable("gameId") Long gameId) throws Exception {
        log.info("Starting game for gameId: " + gameId);
        gameService.setGameToStarted(gameId);
    }

    @RequestMapping(value = "/state/{gameId}/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public GameStateModelDTO getCurrentGameState(@PathVariable("gameId") Long gameId,
         @PathVariable("playerId") Long playerId) throws Exception {
        log.info("Fetching game state for playerId: " + playerId + " and gameid: "+ gameId);
        return gameService.getGameStateModel(gameId, playerId);
    }



    @RequestMapping(value = "/state/perform/place/unit", method = RequestMethod.POST)
    @ResponseBody
    public TerritoryDTO updateGameStateModel(@RequestBody PlaceUnitUpdate placeUnitUpdate) throws NotEnoughUnitsException {
        return gameService.placeUnitAction(placeUnitUpdate);
    }
}
