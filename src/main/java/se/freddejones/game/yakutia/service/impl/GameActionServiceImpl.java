package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.MoveUnitUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.service.BattleCalculator;
import se.freddejones.game.yakutia.service.GameActionService;

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
    public void placeUnitAction(PlaceUnitUpdate placeUnitUpdate) {

    }

    @Override
    public boolean attackTerritoryAction(AttackActionUpdate attackActionUpdate) {
        return false;
    }

    @Override
    public void moveUnitsAction(MoveUnitUpdate placeUnitUpdate) {

    }

    @Override
    public void setActionsToDone(GamePlayerId gamePlayerId) {

    }


//    @Override
//    @Transactional(readOnly = false)
//    public TerritoryDTO placeUnitAction(PlaceUnitUpdate placeUnitUpdate) throws NotEnoughUnitsException {
//        GamePlayer gamePlayer = gamePlayerDao.
//                getGamePlayerByGameIdAndPlayerId(placeUnitUpdate.getPlayerId(), placeUnitUpdate.getGameId());
//
//        Unit unassignedLandUnit = gamePlayerDao.getUnassignedLand(gamePlayer.getGamePlayerId());
//        if (unassignedLandUnit.getStrength()
//                < placeUnitUpdate.getNumberOfUnits()) {
//            throw new NotEnoughUnitsException("Insufficient funds");
//        }
//
//        int strength = 0;
//        for (Unit unit : gamePlayer.getUnits()) {
//            if (unit.getTerritory().equals(Territory.translateLandArea(placeUnitUpdate.getTerritory()))) {
//                strength = unit.getStrength() + placeUnitUpdate.getNumberOfUnits();
//                unit.setStrength(strength);
//                unassignedLandUnit.setStrength(unassignedLandUnit.getStrength()-placeUnitUpdate.getNumberOfUnits());
//                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unit);
//                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unassignedLandUnit);
//            }
//        }
//
//        if (unassignedLandUnit.getStrength() == 0) {
//            gamePlayerDao.setActionStatus(gamePlayer.getGamePlayerId(), ActionStatus.ATTACK);
//        }
//
//        return new TerritoryDTO(placeUnitUpdate.getTerritory(), strength, true);
//    }
//
//    @Override
//    @Transactional(readOnly = false)
//    public TerritoryDTO attackTerritoryAction(AttackActionUpdateDTO attackActionUpdateDTO) throws TerritoryNotConnectedException {
//        Long playerId = attackActionUpdateDTO.getPlayerId();
//        Long gameId = attackActionUpdateDTO.getGameId();
//        GamePlayer attackingGamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
//
//        Territory destionationTerritory = Territory.translateLandArea(attackActionUpdateDTO.getTerritoryAttackDest());
//        if (!GameManager.isTerritoriesConnected(
//                Territory.translateLandArea(attackActionUpdateDTO.getTerritoryAttackSrc()),
//                destionationTerritory)) {
//            throw new TerritoryNotConnectedException("Territories are not connected");
//        }
//
//        GamePlayer defendingGamePlayer = gamePlayerDao.getGamePlayerByGameIdAndUnitId(gameId, destionationTerritory);
//
//        Unit defendingUnit = getUnitByTerritory(attackActionUpdateDTO.getTerritoryAttackDest(), defendingGamePlayer.getUnits());
//        Unit attackingUnit = getUnitByTerritory(attackActionUpdateDTO.getTerritoryAttackSrc(), attackingGamePlayer.getUnits());
//
//        BattleResult battleResult = battleCalculator.battle(attackingUnit, defendingUnit);
//
//        attackingUnit.setStrength(attackingUnit.getStrength()-battleResult.getAttackingTerritoryLosses());
//        defendingUnit.setStrength(defendingUnit.getStrength()-battleResult.getDefendingTerritoryLosses());
//
//
//
//        if (battleResult.isTakenOver()) {
//            defendingUnit.setStrength(attackingUnit.getStrength()-1);
//            attackingUnit.setStrength(1);
//            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), attackingUnit);
//            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), defendingUnit);
//        } else {
//            gamePlayerDao.setUnitsToGamePlayer(defendingGamePlayer.getGamePlayerId(), defendingUnit);
//            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), attackingUnit);
//        }
//
//        TerritoryDTO territoryDTO = new TerritoryDTO(
//                attackActionUpdateDTO.getTerritoryAttackSrc(), attackingUnit.getStrength(), true);
//        return territoryDTO;
//    }
//
//    @Override
//    public String attackTerritoryAction(BattleInformation battleInformation) {
//        return null;
//    }
//
//    @Override
//    public TerritoryDTO moveUnitsAction(PlaceUnitUpdate placeUnitUpdate) {
//        return null;
//    }
//
//    private Unit getUnitByTerritory(String territory, List<Unit> defendingGamePlayerUnits) {
//        for (Unit unit : defendingGamePlayerUnits) {
//            if (unit.getTerritory().equals(Territory.translateLandArea(territory))) {
//                return unit;
//            }
//        }
//        return null;
//    }
}
