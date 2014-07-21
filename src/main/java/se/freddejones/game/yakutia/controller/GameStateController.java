package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.exception.NoGameFoundException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.dto.TerritoryDTO;
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
        List<TerritoryDTO> territoryDTOs = null; //gameStateService.getTerritoryInformationForActiveGame(new GamePlayerId(gamePlayerId));
        if (territoryDTOs.isEmpty()) {
            throw new NoGameFoundException();
        }
        return territoryDTOs;
    }

    @RequestMapping(value = "/{gamePlayerId}", method = RequestMethod.GET)
    @ResponseBody
    public String getCurrentGameState(@PathVariable("gamePlayerId") Long gamePlayerId) {
        LOGGER.info("Fetching game state for playerId: " + gamePlayerId);
        return "not implemented";
    }

}
