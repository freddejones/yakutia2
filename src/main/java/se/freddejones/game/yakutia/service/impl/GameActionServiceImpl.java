package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.CannotMoveUnitException;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.UnitCannotBeFoundException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.MoveUnitUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.application.BattleEngineCalculator;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
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
                gamePlayerDao.updateUnitsToGamePlayer(defendingGamePlayerId,defendingUnit);
            }

            // update for attacking unit
            List<Unit> attackingUnits = unitDao.getUnitsForGamePlayerIdAndTerritory(attackingGamePlayerId, attackActionUpdate.getTerritoryAttackSrc());
            for (Unit attackingUnit : attackingUnits) {
                Integer strength = battleResult.getAttackingUnitsLeft().get(attackingUnit.getTypeOfUnit());
                Integer strengthBefore = attackActionUpdate.getAttackingNumberOfUnits().get(attackingUnit.getTypeOfUnit());
                attackingUnit.addStrength(strength-strengthBefore);
                gamePlayerDao.updateUnitsToGamePlayer(attackingGamePlayerId, attackingUnit);
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
    @Transactional(readOnly = false)
    public void moveUnitsAction(MoveUnitUpdate moveUnitUpdate) {
        GamePlayerId gamePlayerId = moveUnitUpdate.getGamePlayerId();
        List<Unit> unitsToMoveFrom = unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, moveUnitUpdate.getFromTerritory());
        List<Unit> unitsToMoveTo = unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, moveUnitUpdate.getToTerritiory());

        if (unitsToMoveFrom.size() == 0 && unitsToMoveTo.size() ==0) {
            throw new CannotMoveUnitException("Territories are not owned by player: " + moveUnitUpdate.toString());
        }
        validateMoveUnitUpdate(moveUnitUpdate.getUnitsToMove(), unitsToMoveFrom);

        for (Unit u : unitsToMoveFrom) {
            Integer strengthToRemove = moveUnitUpdate.getUnitsToMove().get(u.getTypeOfUnit());
            u.decreaseStrength(strengthToRemove);
            gamePlayerDao.updateUnitsToGamePlayer(gamePlayerId,u);
        }

        for (Unit u : unitsToMoveTo) {
            Integer strengthToAdd = moveUnitUpdate.getUnitsToMove().get(u.getTypeOfUnit());
            u.addStrength(strengthToAdd);
            gamePlayerDao.updateUnitsToGamePlayer(gamePlayerId,u);
        }

    }

    private void validateMoveUnitUpdate(Map<UnitType,Integer> unitsToMove, List<Unit> persistedUnits) {
        int totalUnitsToMove = 0;
        for (Integer strength : unitsToMove.values()) {
            totalUnitsToMove += strength;
        }

        int totalUnitsPersisted = 0;
        for (Unit persistedUnit : persistedUnits) {
            totalUnitsPersisted += persistedUnit.getStrength();
        }

        if (totalUnitsPersisted-totalUnitsToMove <= 0) {
            throw new CannotMoveUnitException("Cannot leave territory empty" + persistedUnits.get(0).getTerritory());
        }
    }

    @Override
    public void updateToNextAction(GamePlayerId gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        ActionStatus actionStatus = gamePlayer.getActionStatus();
        if (actionStatus == ActionStatus.PLACE_UNITS) {
            gamePlayer.setActionStatus(ActionStatus.ATTACK);
        } else if (actionStatus == ActionStatus.ATTACK) {
            gamePlayer.setActionStatus(ActionStatus.MOVE);
        } else if (actionStatus == ActionStatus.MOVE) {
            gamePlayer.setActivePlayerTurn(false);
            GamePlayer nextGamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(new GamePlayerId(gamePlayer.getNextGamePlayerIdTurn()));
            nextGamePlayer.setActivePlayerTurn(true);
            nextGamePlayer.setActionStatus(ActionStatus.PLACE_UNITS);
            gamePlayerDao.updateGamePlayer(nextGamePlayer);
        }
        gamePlayerDao.updateGamePlayer(gamePlayer);
    }
}
