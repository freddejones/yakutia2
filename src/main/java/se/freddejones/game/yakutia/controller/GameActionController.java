package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.dto.PlaceUnitDTO;
import se.freddejones.game.yakutia.model.translators.GameActionBinder;
import se.freddejones.game.yakutia.service.GameActionService;

import java.util.logging.Logger;

@RestController
public class GameActionController {

    private static final Logger log = Logger.getLogger(GameActionController.class.getName());
    private final GameActionService gameActionService;
    private final GameActionBinder gameActionBinder;

    @Autowired
    public GameActionController(GameActionService gameActionService) {
        this.gameActionService = gameActionService;
        this.gameActionBinder = new GameActionBinder();
    }

    @RequestMapping(value = "/perform/place/unit",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void placeUnitOperation(@RequestBody final PlaceUnitDTO placeUnitDTO) {
        log.info(placeUnitDTO.toString());
        PlaceUnitUpdate placeUnitUpdate = gameActionBinder.bind(placeUnitDTO);
        gameActionService.placeUnitAction(placeUnitUpdate);
    }

    @RequestMapping(value = "/perform/attack",
            method = RequestMethod.POST,
            headers = {"content-type=application/json"},
            consumes = "application/json")
    @ResponseBody
    public String attackTerritoryO(@RequestBody final AttackActionUpdate attackActionUpdate) {
//        log.info(attackActionUpdate.toString());
//        BattleInformation battleInformation = battleInformationBinder.map(attackActionUpdate);
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
        log.info(attackActionUpdate.toString());
        gameActionService.attackTerritoryAction(attackActionUpdate);
    }

}
