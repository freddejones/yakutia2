package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.UnitCannotBeFoundException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.MoveUnitUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.application.BattleEngineCalculator;
import se.freddejones.game.yakutia.service.GameActionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameActionServiceImpl implements GameActionService {

    private final GamePlayerDao gamePlayerDao;
    private final UnitDao unitDao;
    private final BattleEngineCalculator battleCalculator;

    @Autowired
    public GameActionServiceImpl(GamePlayerDao gamePlayerDao, UnitDao unitDao, BattleEngineCalculator battleCalculator) {
        this.gamePlayerDao = gamePlayerDao;
        this.unitDao = unitDao;
        this.battleCalculator = battleCalculator;
    }

    @Override
    @Transactional(readOnly = false)
    public void placeUnitAction(PlaceUnitUpdate placeUnitUpdate) {
        GamePlayerId gamePlayerId = placeUnitUpdate.getGamePlayerId();
        Unit unassignedUnit = validateUnitUpdate(placeUnitUpdate, gamePlayerId);

        Unit unit = extractUnitToUpdate(gamePlayerId, placeUnitUpdate.getTerritory(), placeUnitUpdate.getUnitType());
        unit.addStrength(placeUnitUpdate.getNumberOfUnits());
        unassignedUnit.addStrength(-placeUnitUpdate.getNumberOfUnits());

        gamePlayerDao.updateUnitsToGamePlayer(gamePlayerId, unit);
        gamePlayerDao.updateUnitsToGamePlayer(gamePlayerId, unassignedUnit);
    }

    private Unit extractUnitToUpdate(GamePlayerId gamePlayerId, Territory territory, UnitType unitType) {
        List<Unit> units = unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, territory);
        for (Unit u : units) {
            if (u.getTypeOfUnit() == unitType) {
                return u;
            }
        }
        throw new UnitCannotBeFoundException("Could not find unit " + unitType.toString() + " for territory " + territory.toString());
    }

    private Unit validateUnitUpdate(PlaceUnitUpdate placeUnitUpdate, GamePlayerId gamePlayerId) {
        List<Unit> unassignedUnits = unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.UNASSIGNED_TERRITORY);
        for (Unit u : unassignedUnits) {
            boolean unitTypeIsEqual = u.getTypeOfUnit() == placeUnitUpdate.getUnitType();
            if (unitTypeIsEqual && u.getStrength() < placeUnitUpdate.getNumberOfUnits()) {
                throw new NotEnoughUnitsException("Not enough units for type: " + placeUnitUpdate.getUnitType().toString());
            } else if (unitTypeIsEqual) {
                return u;
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public void attackTerritoryAction(AttackActionUpdate attackActionUpdate) {
        GamePlayerId attackingGamePlayerId = attackActionUpdate.getGamePlayerId();
        GamePlayerId defendingGamePlayerId = attackActionUpdate.getDefendingGamePlayerId();
        List<Unit> defendingUnits = unitDao.getUnitsForGamePlayerIdAndTerritory(defendingGamePlayerId, attackActionUpdate.getTerritoryAttackDest());
        BattleResult battleResult = battleCalculator.battleForTerritory(attackActionUpdate.getAttackingNumberOfUnits(), mapUnitsToAttackStructure(defendingUnits));

        if (battleResult.isTakenOver()) {
            for (Unit defendingUnit : defendingUnits) {
                unitDao.setGamePlayerIdForUnit(attackingGamePlayerId, new UnitId(defendingUnit.getUnitId()));
                Integer strength = battleResult.getAttackingUnitsLeft().get(defendingUnit.getTypeOfUnit());
                defendingUnit.setStrength(strength);
                gamePlayerDao.updateUnitsToGamePlayer(attackingGamePlayerId,defendingUnit);
            }
        } else {

            // update for defending unit
            for (Unit defendingUnit : defendingUnits) {
                Integer strength = battleResult.getDefendingUnitsLeft().get(defendingUnit.getTypeOfUnit());
                defendingUnit.setStrength(strength);
                gamePlayerDao.updateUnitsToGamePlayer(attackingGamePlayerId,defendingUnit);
            }

            // update for attacking unit
            List<Unit> attackingUnits = unitDao.getUnitsForGamePlayerIdAndTerritory(attackingGamePlayerId, attackActionUpdate.getTerritoryAttackSrc());
            for (Unit attackingUnit : attackingUnits) {
                Integer strength = battleResult.getAttackingUnitsLeft().get(attackingUnit.getTypeOfUnit());
                Integer strengthBefore = attackActionUpdate.getAttackingNumberOfUnits().get(attackingUnit.getTypeOfUnit());
                attackingUnit.addStrength(strength-strengthBefore);
                gamePlayerDao.updateUnitsToGamePlayer(attackingGamePlayerId,attackingUnit);
            }
        }


    }

    private Map<UnitType, Integer> mapUnitsToAttackStructure(List<Unit> units) {
        Map<UnitType, Integer> mappedUnits = new HashMap<>();
        for (Unit unit : units) {
            mappedUnits.put(unit.getTypeOfUnit(), unit.getStrength());
        }
        return mappedUnits;
    }

    @Override
    public void moveUnitsAction(MoveUnitUpdate placeUnitUpdate) {

    }

    @Override
    public void setActionsToDone(GamePlayerId gamePlayerId) {

    }
}
