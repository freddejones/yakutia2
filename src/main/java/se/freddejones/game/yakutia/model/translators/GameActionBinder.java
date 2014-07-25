package se.freddejones.game.yakutia.model.translators;

import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.dto.AttackActionDTO;
import se.freddejones.game.yakutia.model.dto.MoveUnitUpdateDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitDTO;

import java.util.HashMap;
import java.util.Map;

public class GameActionBinder {

    public PlaceUnitUpdate bind(PlaceUnitDTO placeUnitDTO) {
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate();
        placeUnitUpdate.setGamePlayerId(new GamePlayerId(placeUnitDTO.getGamePlayerId()));
        placeUnitUpdate.setNumberOfUnits(placeUnitDTO.getNumberOfUnits().intValue());
        placeUnitUpdate.setTerritory(Territory.valueOf(placeUnitDTO.getTerritory()));
        placeUnitUpdate.setUnitType(UnitType.valueOf(placeUnitDTO.getUnitType()));
        return placeUnitUpdate;
    }

    public AttackActionUpdate bind(AttackActionDTO attackActionDTO) {
        AttackActionUpdate attackActionUpdate = new AttackActionUpdate();
        attackActionUpdate.setTerritoryAttackDest(Territory.valueOf(attackActionDTO.getTerritoryAttackDst()));
        attackActionUpdate.setTerritoryAttackSrc(Territory.valueOf(attackActionDTO.getTerritoryAttackSrc()));
        attackActionUpdate.setGamePlayerId(new GamePlayerId(attackActionDTO.getAttackingGamePlayerId()));
        attackActionUpdate.setDefendingGamePlayerId(new GamePlayerId(attackActionDTO.getDefendingGamePlayerId()));
        attackActionUpdate.setAttackingNumberOfUnits(mapUnits(attackActionDTO.getAttackingUnits()));
        return attackActionUpdate;
    }

    public MoveUnitUpdate bind(MoveUnitUpdateDTO moveUnitUpdateDTO) {
        MoveUnitUpdate moveUnitUpdate = new MoveUnitUpdate();
        moveUnitUpdate.setGamePlayerId(new GamePlayerId(moveUnitUpdateDTO.getGamePlayerId()));
        moveUnitUpdate.setFromTerritory(Territory.valueOf(moveUnitUpdateDTO.getFromTerritory()));
        moveUnitUpdate.setToTerritiory(Territory.valueOf(moveUnitUpdateDTO.getToTerritory()));
        moveUnitUpdate.setUnitsToMove(mapUnits(moveUnitUpdateDTO.getUnits()));
        return moveUnitUpdate;
    }

    private Map<UnitType, Integer> mapUnits(Map<String, Integer> dtoUnits) {
        Map<UnitType, Integer> units = new HashMap<>();
        for (Map.Entry<String, Integer> entry : dtoUnits.entrySet()) {
            units.put(UnitType.valueOf(entry.getKey()), entry.getValue());
        }
        return units;
    }
}
