package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.MoveUnitUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.dto.AttackActionDTO;
import se.freddejones.game.yakutia.model.dto.MoveUnitUpdateDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitDTO;
import se.freddejones.game.yakutia.model.translators.GameActionBinder;
import se.freddejones.game.yakutia.service.GameActionService;

@RestController
public class GameActionController {

    private final GameActionService gameActionService;
    private final GameActionBinder gameActionBinder;

    @Autowired
    public GameActionController(GameActionService gameActionService) {
        this.gameActionService = gameActionService;
        this.gameActionBinder = new GameActionBinder();
    }

    @RequestMapping(value = "/game/action/place", method = RequestMethod.POST)
    @ResponseBody
    public void placeUnitOperation(@RequestBody final PlaceUnitDTO placeUnitDTO) {
        PlaceUnitUpdate placeUnitUpdate = gameActionBinder.bind(placeUnitDTO);
        gameActionService.placeUnitAction(placeUnitUpdate);
    }

    @RequestMapping(value = "/game/action/attack", method = RequestMethod.POST)
    @ResponseBody
    public void attackTerritory(@RequestBody final AttackActionDTO attackActionDTO) {
        AttackActionUpdate attackActionUpdate = gameActionBinder.bind(attackActionDTO);
        gameActionService.attackTerritoryAction(attackActionUpdate);
    }

    @RequestMapping(value = "/game/action/move", method = RequestMethod.POST)
    @ResponseBody
    public void attackTerritory(@RequestBody final MoveUnitUpdateDTO moveUnitUpdateDTO) {
        MoveUnitUpdate moveUnitUpdate = gameActionBinder.bind(moveUnitUpdateDTO);
        gameActionService.moveUnitsAction(moveUnitUpdate);
    }
}
