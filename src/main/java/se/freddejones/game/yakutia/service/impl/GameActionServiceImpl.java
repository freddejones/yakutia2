package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.service.GameActionService;

import java.util.List;

@Service
public class GameActionServiceImpl implements GameActionService {

    private GamePlayerDao gamePlayerDao;
    private BattleCalculator battleCalculator;

    @Autowired
    public GameActionServiceImpl(GamePlayerDao gamePlayerDao, BattleCalculator battleCalculator) {
        this.gamePlayerDao = gamePlayerDao;
        this.battleCalculator = battleCalculator;
    }

    @Override
    @Transactional(readOnly = false)
    public TerritoryDTO placeUnitAction(PlaceUnitUpdate placeUnitUpdate) throws NotEnoughUnitsException {
        GamePlayer gamePlayer = gamePlayerDao.
                getGamePlayerByGameIdAndPlayerId(placeUnitUpdate.getPlayerId(), placeUnitUpdate.getGameId());

        Unit unassignedLandUnit = gamePlayerDao.getUnassignedLand(gamePlayer.getGamePlayerId());
        if (unassignedLandUnit.getStrength()
                < placeUnitUpdate.getNumberOfUnits()) {
            throw new NotEnoughUnitsException("Insufficient funds");
        }

        int strength = 0;
        for (Unit unit : gamePlayer.getUnits()) {
            if (unit.getTerritory().equals(Territory.translateLandArea(placeUnitUpdate.getTerritory()))) {
                strength = unit.getStrength() + placeUnitUpdate.getNumberOfUnits();
                unit.setStrength(strength);
                unassignedLandUnit.setStrength(unassignedLandUnit.getStrength()-placeUnitUpdate.getNumberOfUnits());
                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unit);
                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unassignedLandUnit);
            }
        }

        if (unassignedLandUnit.getStrength() == 0) {
            gamePlayerDao.setActionStatus(gamePlayer.getGamePlayerId(), ActionStatus.ATTACK);
        }

        return new TerritoryDTO(placeUnitUpdate.getTerritory(), strength, true);
    }

    @Override
    @Transactional(readOnly = false)
    public TerritoryDTO attackTerritoryAction(AttackActionUpdate attackActionUpdate) throws TerritoryNotConnectedException {
        Long playerId = attackActionUpdate.getPlayerId();
        Long gameId = attackActionUpdate.getGameId();
        GamePlayer attackingGamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);

        Territory destionationTerritory = Territory.translateLandArea(attackActionUpdate.getTerritoryAttackDest());
        if (!GameManager.isTerritoriesConnected(
                Territory.translateLandArea(attackActionUpdate.getTerritoryAttackSrc()),
                destionationTerritory)) {
            throw new TerritoryNotConnectedException("Territories are not connected");
        }

        GamePlayer defendingGamePlayer = gamePlayerDao.getGamePlayerByGameIdAndTerritory(gameId, destionationTerritory);

        Unit defendingUnit = getUnitByTerritory(attackActionUpdate.getTerritoryAttackDest(), defendingGamePlayer.getUnits());
        Unit attackingUnit = getUnitByTerritory(attackActionUpdate.getTerritoryAttackSrc(), attackingGamePlayer.getUnits());

        BattleResult battleResult = battleCalculator.battle(attackingUnit, defendingUnit);

        attackingUnit.setStrength(attackingUnit.getStrength()-battleResult.getAttackingTerritoryLosses());
        defendingUnit.setStrength(defendingUnit.getStrength()-battleResult.getDefendingTerritoryLosses());



        if (battleResult.isTakenOver()) {
            defendingUnit.setStrength(attackingUnit.getStrength()-1);
            attackingUnit.setStrength(1);
            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), attackingUnit);
            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), defendingUnit);
        } else {
            gamePlayerDao.setUnitsToGamePlayer(defendingGamePlayer.getGamePlayerId(), defendingUnit);
            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), attackingUnit);
        }

        TerritoryDTO territoryDTO = new TerritoryDTO(
                attackActionUpdate.getTerritoryAttackSrc(), attackingUnit.getStrength(), true);
        return territoryDTO;
    }

    @Override
    public TerritoryDTO moveUnitsAction(PlaceUnitUpdate placeUnitUpdate) {
        return null;
    }

    private Unit getUnitByTerritory(String territory, List<Unit> defendingGamePlayerUnits) {
        for (Unit unit : defendingGamePlayerUnits) {
            if (unit.getTerritory().equals(Territory.translateLandArea(territory))) {
                return unit;
            }
        }
        return null;
    }
}
