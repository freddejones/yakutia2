package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.TerritoryInformation;
import se.freddejones.game.yakutia.model.dto.TerritoryDTO;
import se.freddejones.game.yakutia.model.translators.GameInformationBinder;
import se.freddejones.game.yakutia.service.GameStateService;

import java.util.List;

@RestController
public class GameStateController {

    private final GameStateService gameStateService;
    private final GameInformationBinder gameInformationBinder;

    @Autowired
    public GameStateController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
        this.gameInformationBinder = new GameInformationBinder();
    }

    @RequestMapping(value = "/state/gameplayer/{gamePlayerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<TerritoryDTO> getTerritoryState(@PathVariable("gamePlayerId") final Long gamePlayerId) {
        List<TerritoryInformation> territoryInformations = gameStateService.getTerritoryInformationForActiveGame(new GamePlayerId(gamePlayerId));
        return gameInformationBinder.bind(territoryInformations);
    }

}
