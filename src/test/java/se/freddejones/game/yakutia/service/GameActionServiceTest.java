package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import se.freddejones.game.yakutia.application.BattleEngineCalculator;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.UnitCannotBeFoundException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.service.impl.GameActionServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GameActionServiceTest {

    private GamePlayerDao gamePlayerDao;
    private UnitDao unitDao;
    private GameActionService gameActionService;
    private BattleEngineCalculator battleEngineCalculator;
    private GamePlayerId gamePlayerId;

    @Before
    public void setup() {
        gamePlayerId = new GamePlayerId(1L);
        gamePlayerDao = mock(GamePlayerDao.class);
        battleEngineCalculator = mock(BattleEngineCalculator.class);
        unitDao = mock(UnitDao.class);
        gameActionService = new GameActionServiceImpl(gamePlayerDao, unitDao, battleEngineCalculator);
    }

    @Test
    public void testUnitStrengthGetsAddedToUnit() {

        // given
        PlaceUnitUpdate placeUnitUpdate = getDefaultPlaceUnitUpdate();
        Unit unit = createDefaultUnit(1);

        Unit unitToUpdate = createDefaultUnit(1);
        unitToUpdate.setTerritory(Territory.SWEDEN);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.UNASSIGNED_TERRITORY)).thenReturn(Arrays.asList(unit));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.SWEDEN)).thenReturn(Arrays.asList(unitToUpdate));


        // when
        gameActionService.placeUnitAction(placeUnitUpdate);

        // then
        ArgumentCaptor<Unit> unitCaptor = ArgumentCaptor.forClass(Unit.class);
        verify(gamePlayerDao, times(2)).updateUnitsToGamePlayer(any(GamePlayerId.class), unitCaptor.capture());
        assertThat(unitCaptor.getAllValues().get(0).getStrength(), is(2));
    }

    @Test
    public void testUnitStrengthGetsSubtractedFromUnit() {

        // given
        PlaceUnitUpdate placeUnitUpdate = getDefaultPlaceUnitUpdate();
        Unit unit = createDefaultUnit(1);
        Unit unitToUpdate = createDefaultUnit(1);
        unitToUpdate.setTerritory(Territory.SWEDEN);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.UNASSIGNED_TERRITORY)).thenReturn(Arrays.asList(unit));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.SWEDEN)).thenReturn(Arrays.asList(unitToUpdate));


        // when
        gameActionService.placeUnitAction(placeUnitUpdate);

        // then
        ArgumentCaptor<Unit> unitCaptor = ArgumentCaptor.forClass(Unit.class);
        verify(gamePlayerDao, times(2)).updateUnitsToGamePlayer(any(GamePlayerId.class), unitCaptor.capture());
        assertThat(unitCaptor.getAllValues().get(1).getStrength(), is(0));

    }

    @Test(expected = UnitCannotBeFoundException.class)
    public void testCannotFindUnitWhenUpdate() {

        // given
        PlaceUnitUpdate placeUnitUpdate = getDefaultPlaceUnitUpdate();
        Unit unit = createDefaultUnit(1);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.UNASSIGNED_TERRITORY)).thenReturn(Arrays.asList(unit));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.SWEDEN)).thenReturn(new ArrayList<Unit>());

        // when
        gameActionService.placeUnitAction(placeUnitUpdate);
    }

    @Test(expected = NotEnoughUnitsException.class)
    public void testPlaceUnitForUnsufficientFunds() {

        // given
        PlaceUnitUpdate placeUnitUpdate = getDefaultPlaceUnitUpdate();
        Unit unit = createDefaultUnit(0);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.UNASSIGNED_TERRITORY)).thenReturn(Arrays.asList(unit));

        // when
        gameActionService.placeUnitAction(placeUnitUpdate);

    }

    @Test
    public void testAttackTerritoryActionIsTakenOver() {

        ArgumentCaptor<Unit> unitArgumentCaptor = ArgumentCaptor.forClass(Unit.class);
        ArgumentCaptor<GamePlayerId> gamePlayerIdArgumentCaptor = ArgumentCaptor.forClass(GamePlayerId.class);

        // given
        AttackActionUpdate attackActionUpdate = createDefaultAttackActionUpdate();
        BattleResult battleResult = new BattleResult(attackActionUpdate.getAttackingNumberOfUnits(), new HashMap<UnitType, Integer>(), true);
        Unit unit = new Unit();
        unit.setStrength(2);
        unit.setTerritory(Territory.DENMARK);
        unit.setTypeOfUnit(UnitType.SOLDIER);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(any(GamePlayerId.class), eq(Territory.DENMARK))).thenReturn(Arrays.asList(unit));
        when(battleEngineCalculator.battleForTerritory(any(HashMap.class), any(HashMap.class))).thenReturn(battleResult);

        // when
        gameActionService.attackTerritoryAction(attackActionUpdate);

        // then
        verify(unitDao, times(1)).setGamePlayerIdForUnit(eq(gamePlayerId), any(UnitId.class));
        verify(gamePlayerDao, times(1)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
        assertThat(gamePlayerIdArgumentCaptor.getValue(), is(gamePlayerId));
        assertThat(unitArgumentCaptor.getValue().getStrength(), is(5));
    }

    @Test
    public void testAttackTerritoryActionIsNotTakenOver() {
//        ArgumentCaptor<Unit> unitArgumentCaptor = ArgumentCaptor.forClass(Unit.class);
//        ArgumentCaptor<GamePlayerId> gamePlayerIdArgumentCaptor = ArgumentCaptor.forClass(GamePlayerId.class);
//
//        // given
//        AttackActionUpdate attackActionUpdate = createDefaultAttackActionUpdate();
//        Map<UnitType,Integer> defendingUnits = attackActionUpdate.getAttackingNumberOfUnits();
//        BattleResult battleResult = new BattleResult(attackActionUpdate.getAttackingNumberOfUnits(),defendingUnits, false);
//        Unit unit = new Unit();
//        unit.setStrength(10);
//        unit.setTerritory(Territory.DENMARK);
//        unit.setTypeOfUnit(UnitType.SOLDIER);
//        when(unitDao.getUnitsForGamePlayerIdAndTerritory(any(GamePlayerId.class), eq(Territory.DENMARK))).thenReturn(Arrays.asList(unit));
//        when(battleEngineCalculator.battleForTerritory(any(HashMap.class), any(HashMap.class))).thenReturn(battleResult);
//
//        // when
//        gameActionService.attackTerritoryAction(attackActionUpdate);
//
//        // then
//        verify(unitDao, times(1)).setGamePlayerIdForUnit(eq(gamePlayerId), any(UnitId.class));
//        verify(gamePlayerDao, times(1)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
//        assertThat(gamePlayerIdArgumentCaptor.getValue(), is(gamePlayerId));
//        assertThat(unitArgumentCaptor.getValue().getStrength(), is(5));
    }

    private AttackActionUpdate createDefaultAttackActionUpdate() {
        AttackActionUpdate attackActionUpdate = new AttackActionUpdate();
        attackActionUpdate.setGamePlayerId(gamePlayerId);
        attackActionUpdate.setDefendingGamePlayerId(new GamePlayerId(2L));
        Map<UnitType, Integer> attackingUnits = new HashMap<>();
        attackingUnits.put(UnitType.SOLDIER, 5);
        attackActionUpdate.setAttackingNumberOfUnits(attackingUnits);
        attackActionUpdate.setTerritoryAttackDest(Territory.DENMARK);
        attackActionUpdate.setTerritoryAttackSrc(Territory.SWEDEN);
        return attackActionUpdate;
    }

    private PlaceUnitUpdate getDefaultPlaceUnitUpdate() {
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate();
        placeUnitUpdate.setNumberOfUnits(1);
        placeUnitUpdate.setUnitType(UnitType.SOLDIER);
        placeUnitUpdate.setTerritory(Territory.SWEDEN);
        placeUnitUpdate.setGamePlayerId(gamePlayerId);
        return placeUnitUpdate;
    }

    private Unit createDefaultUnit(int strength) {
        Unit unit = new Unit();
        unit.setTypeOfUnit(UnitType.SOLDIER);
        unit.setStrength(strength);
        unit.setTerritory(Territory.UNASSIGNED_TERRITORY);
        return unit;
    }
}
