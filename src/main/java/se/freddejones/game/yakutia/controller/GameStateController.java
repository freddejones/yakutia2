package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.exception.NoGameFoundException;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.service.GameActionService;
import se.freddejones.game.yakutia.service.GameService;
import se.freddejones.game.yakutia.service.GameStateService;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/state")
public class GameStateController {

    private static final Logger LOGGER = Logger.getLogger(GameStateController.class.getName());
    private GameStateService gameStateService;
    private GameActionService gameActionService;

    @Autowired
    public GameStateController(GameStateService gameStateService, GameActionService gameActionService) {
        this.gameStateService = gameStateService;
        this.gameActionService = gameActionService;
    }

    @RequestMapping(value = "/get/{playerId}/game/{gameId}", method = RequestMethod.GET)
    @ResponseBody
    public List<TerritoryDTO> getGame(@PathVariable("playerId") Long playerId,
                                      @PathVariable("gameId") Long gameId) {
        LOGGER.info("Getting game information for gameId: " + gameId + " and playerId: " + playerId);
        List<TerritoryDTO> territoryDTOs = gameStateService.getTerritoryInformationForActiveGame(playerId, gameId);
        if (territoryDTOs.isEmpty()) {
            throw new NoGameFoundException();
        }
        return territoryDTOs;
    }

    @RequestMapping(value = "/{gameId}/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public GameStateModelDTO getCurrentGameState(@PathVariable("gameId") Long gameId,
                                                 @PathVariable("playerId") Long playerId) throws Exception {
        LOGGER.info("Fetching game state for playerId: " + playerId + " and gameid: " + gameId);
        return gameStateService.getGameStateModel(gameId, playerId);
    }

    @RequestMapping(value = "/territory/{gameId}/{playerId}/{territory}",
            method = RequestMethod.GET)
    @ResponseBody
    public TerritoryDTO getCurrentTerritoryState(@PathVariable("gameId") Long gameId,
                                                 @PathVariable("playerId") Long playerId,
                                                 @PathVariable("territory") String territory) {
        return gameStateService.getTerritoryInformationForTerritory(Territory.translateLandArea(territory), gameId, playerId);
    }

    @RequestMapping(value = "/perform/place/unit",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public TerritoryDTO placeUnitOperation(@RequestBody final PlaceUnitUpdate placeUnitUpdate) {
        LOGGER.info(placeUnitUpdate.toString());
        return gameActionService.placeUnitAction(placeUnitUpdate);
    }

    @RequestMapping(value = "/perform/attack/territory",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public TerritoryDTO attackTerritoryOperation(@RequestBody final AttackActionUpdate attackActionUpdate) {
        LOGGER.info(attackActionUpdate.toString());
        return gameActionService.attackTerritoryAction(attackActionUpdate);
    }
}
