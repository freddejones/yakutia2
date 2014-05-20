package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/get/{playerId}/game/{gameId}", method = RequestMethod.GET)
    @ResponseBody
    public List<TerritoryDTO> getGame(@PathVariable("playerId") Long playerId,
                                       @PathVariable("gameId") Long gameId) {
        LOGGER.info("Getting game information for gameId: " + gameId + " and playerId: " + playerId);
        List<TerritoryDTO> territoryDTOs = gameService.getTerritoryInformationForActiveGame(playerId, gameId);
        if (territoryDTOs.isEmpty()) {
            throw new NoGameFoundException();
        }
        return territoryDTOs;
    }

    @RequestMapping(value = "/start/{gameId}", method = RequestMethod.PUT)
    @ResponseBody
    public void startGame(@PathVariable("gameId") Long gameId) throws Exception {
        LOGGER.info("Starting game for gameId: " + gameId);
        gameService.setGameToStarted(gameId);
    }



    // TODO refactor this to GAmestateController instead
    @RequestMapping(value = "/state/{gameId}/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public GameStateModelDTO getCurrentGameState(@PathVariable("gameId") Long gameId,
         @PathVariable("playerId") Long playerId) throws Exception {
        LOGGER.info("Fetching game state for playerId: " + playerId + " and gameid: " + gameId);
        return gameService.getGameStateModel(gameId, playerId);
    }

    @RequestMapping(value = "/state/territory/{gameId}/{playerId}/{territory}",
            method = RequestMethod.GET)
    @ResponseBody
    public TerritoryDTO getCurrentTerritoryState(@PathVariable("gameId") Long gameId,
                                                 @PathVariable("playerId") Long playerId,
                                                 @PathVariable("territory") String territory) {
        return gameService.getTerritoryInformationForTerritory(Territory.translateLandArea(territory), gameId, playerId);
    }

    @RequestMapping(value = "/state/perform/place/unit",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public TerritoryDTO placeUnitOperation(@RequestBody final PlaceUnitUpdate placeUnitUpdate) {
        LOGGER.info(placeUnitUpdate.toString());
        return gameService.placeUnitAction(placeUnitUpdate);
    }

    @RequestMapping(value = "/state/perform/attack/territory",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public TerritoryDTO attackTerritoryOperation(@RequestBody final AttackActionUpdate attackActionUpdate) {
        LOGGER.info(attackActionUpdate.toString());
        return gameService.attackTerritoryAction(attackActionUpdate);
    }
}
