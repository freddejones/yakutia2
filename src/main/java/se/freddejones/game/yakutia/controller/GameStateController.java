package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.service.GameService;

import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/state")
public class GameStateController {

    private static final Logger LOGGER = Logger.getLogger(GameStateController.class.getName());
    private GameService gameService;

    @Autowired
    public GameStateController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/{gameId}/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public GameStateModelDTO getCurrentGameState(@PathVariable("gameId") Long gameId,
                                                 @PathVariable("playerId") Long playerId) throws Exception {
        LOGGER.info("Fetching game state for playerId: " + playerId + " and gameid: " + gameId);
        return gameService.getGameStateModel(gameId, playerId);
    }

    @RequestMapping(value = "/territory/{gameId}/{playerId}/{territory}",
            method = RequestMethod.GET)
    @ResponseBody
    public TerritoryDTO getCurrentTerritoryState(@PathVariable("gameId") Long gameId,
                                                 @PathVariable("playerId") Long playerId,
                                                 @PathVariable("territory") String territory) {
        return gameService.getTerritoryInformationForTerritory(Territory.translateLandArea(territory), gameId, playerId);
    }

    @RequestMapping(value = "/perform/place/unit",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public TerritoryDTO placeUnitOperation(@RequestBody final PlaceUnitUpdate placeUnitUpdate) {
        LOGGER.info(placeUnitUpdate.toString());
        return gameService.placeUnitAction(placeUnitUpdate);
    }

    @RequestMapping(value = "/perform/attack/territory",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public TerritoryDTO attackTerritoryOperation(@RequestBody final AttackActionUpdate attackActionUpdate) {
        LOGGER.info(attackActionUpdate.toString());
        return gameService.attackTerritoryAction(attackActionUpdate);
    }
}
