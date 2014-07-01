package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.exception.NoGameFoundException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.dto.PlaceUnitDTO;
import se.freddejones.game.yakutia.model.translators.BattleInformationBinder;
import se.freddejones.game.yakutia.model.translators.GameActionBinder;
import se.freddejones.game.yakutia.service.GameActionService;
import se.freddejones.game.yakutia.service.GameStateService;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/state")
public class GameStateController {

    private static final Logger LOGGER = Logger.getLogger(GameStateController.class.getName());
    private final GameStateService gameStateService;
    private final GameActionService gameActionService;
    private final BattleInformationBinder battleInformationBinder;
    private final GameActionBinder gameActionBinder;

    @Autowired
    public GameStateController(GameStateService gameStateService, GameActionService gameActionService) {
        this.gameStateService = gameStateService;
        this.gameActionService = gameActionService;
        this.battleInformationBinder = new BattleInformationBinder();
        this.gameActionBinder = new GameActionBinder();
    }

    @RequestMapping(value = "/get/game/{gamePlayerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<TerritoryDTO> getGame(@PathVariable("gamePlayerId") Long gamePlayerId) {
        LOGGER.info("Getting game information for gamePlayerId: " + gamePlayerId);
        List<TerritoryDTO> territoryDTOs = gameStateService.getTerritoryInformationForActiveGame(new GamePlayerId(gamePlayerId));
        if (territoryDTOs.isEmpty()) {
            throw new NoGameFoundException();
        }
        return territoryDTOs;
    }

    @RequestMapping(value = "/{gamePlayerId}", method = RequestMethod.GET)
    @ResponseBody
    public GameStateModelDTO getCurrentGameState(@PathVariable("gamePlayerId") Long gamePlayerId) {
        LOGGER.info("Fetching game state for playerId: " + gamePlayerId);
        return gameStateService.getGameStateModel(new GamePlayerId(gamePlayerId));
    }

    @RequestMapping(value = "/territory/{gamePlayerId}/{territoryId}",
            method = RequestMethod.GET)
    @ResponseBody
    public TerritoryDTO getCurrentTerritoryState(@PathVariable("gamePlayerId") Long gamePlayerId,
                                                 @PathVariable("territoryId") Integer territoryId) {

        return gameStateService.getTerritoryInformation(new UnitId(territoryId), new GamePlayerId(gamePlayerId));
    }

    @RequestMapping(value = "/perform/place/unit",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void placeUnitOperation(@RequestBody final PlaceUnitDTO placeUnitDTO) {
        LOGGER.info(placeUnitDTO.toString());
        PlaceUnitUpdate placeUnitUpdate = gameActionBinder.bind(placeUnitDTO);
        gameActionService.placeUnitAction(placeUnitUpdate);
    }

    @RequestMapping(value = "/perform/attack",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public String attackTerritoryO(@RequestBody final AttackActionUpdate attackActionUpdate) {
//        LOGGER.info(attackActionUpdate.toString());
//        BattleInformation battleInformation = battleInformationBinder.bind(attackActionUpdate);
//        return gameActionService.attackTerritoryAction();
        return "";
    }

    // TODO remove this old url
    @RequestMapping(value = "/perform/attack/territory",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public void attackTerritoryOperation(@RequestBody final AttackActionUpdate attackActionUpdate) {
        LOGGER.info(attackActionUpdate.toString());
        gameActionService.attackTerritoryAction(attackActionUpdate);
    }
}
