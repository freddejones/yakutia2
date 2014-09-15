package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import se.freddejones.game.yakutia.application.BattleEngineCalculator;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.CannotMoveUnitException;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.UnitCannotBeFoundException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.service.impl.GameActionServiceImpl;

import java.util.*;

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
    private GamePlayerId defendingGamePlayerId;

    @Before
    public void setup() {
        gamePlayerId = new GamePlayerId(1L);
        defendingGamePlayerId = new GamePlayerId(2L);
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
        Unit unit = createSoldierUnit(2, Territory.DENMARK);
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
        ArgumentCaptor<Unit> unitArgumentCaptor = ArgumentCaptor.forClass(Unit.class);
        ArgumentCaptor<GamePlayerId> gamePlayerIdArgumentCaptor = ArgumentCaptor.forClass(GamePlayerId.class);

        // given
        Unit defendingSideStoredUnits = createSoldierUnit(10, Territory.DENMARK);
        Unit attackingSideStoredUnits = createSoldierUnit(7, Territory.SWEDEN);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(eq(defendingGamePlayerId), eq(Territory.DENMARK))).thenReturn(Arrays.asList(defendingSideStoredUnits));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(eq(gamePlayerId), eq(Territory.SWEDEN))).thenReturn(Arrays.asList(attackingSideStoredUnits));


        AttackActionUpdate attackActionUpdate = createDefaultAttackActionUpdate();
        Map<UnitType,Integer> defendingUnitsLeft = new HashMap<>();
        defendingUnitsLeft.put(UnitType.SOLDIER, 8);
        Map<UnitType,Integer> attackingUnitsLeftFromAttack = new HashMap<>();
        attackingUnitsLeftFromAttack.put(UnitType.SOLDIER, 3);
        BattleResult battleResult = new BattleResult(attackingUnitsLeftFromAttack, defendingUnitsLeft, false);
        when(battleEngineCalculator.battleForTerritory(any(HashMap.class), any(HashMap.class))).thenReturn(battleResult);

        // when
        gameActionService.attackTerritoryAction(attackActionUpdate);

        // then
        verify(unitDao, times(1)).getUnitsForGamePlayerIdAndTerritory(eq(gamePlayerId), eq(Territory.SWEDEN));
        verify(gamePlayerDao, times(2)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());

        // assert calls
        assertThat(gamePlayerIdArgumentCaptor.getAllValues().get(0), is(defendingGamePlayerId));
        assertThat(gamePlayerIdArgumentCaptor.getAllValues().get(1), is(gamePlayerId));

        assertThat(unitArgumentCaptor.getAllValues().get(0).getStrength(), is(8));
        assertThat(unitArgumentCaptor.getAllValues().get(1).getStrength(), is(5));
    }

    @Test
    public void testWhenMoveUnitsToOwnedTerritory() {
        ArgumentCaptor<GamePlayerId> gamePlayerIdArgumentCaptor = ArgumentCaptor.forClass(GamePlayerId.class);
        ArgumentCaptor<Unit> unitArgumentCaptor = ArgumentCaptor.forClass(Unit.class);

        // given
        MoveUnitUpdate moveUnitUpdate = new MoveUnitUpdate();
        moveUnitUpdate.setFromTerritory(Territory.DENMARK);
        moveUnitUpdate.setToTerritiory(Territory.ICELAND);
        moveUnitUpdate.setGamePlayerId(gamePlayerId);
        Map<UnitType, Integer> unitsToMove = new HashMap<>();
        unitsToMove.put(UnitType.SOLDIER, 4);
        moveUnitUpdate.setUnitsToMove(unitsToMove);
        List<Unit> persistedUnitsFromTerritory = Arrays.asList(createSoldierUnit(5, Territory.DENMARK));
        List<Unit> persistedUnitsToTerritory = Arrays.asList(createSoldierUnit(5, Territory.ICELAND));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(eq(gamePlayerId), eq(Territory.DENMARK))).thenReturn(persistedUnitsFromTerritory);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(eq(gamePlayerId), eq(Territory.ICELAND))).thenReturn(persistedUnitsToTerritory);
        GamePlayer gamePlayer = new GamePlayer();
        List<Unit> units = new ArrayList<>();
        for (Unit unit : persistedUnitsToTerritory) {
            units.add(unit);
        }
        for (Unit unit : persistedUnitsFromTerritory) {
            units.add(unit);
        }
        gamePlayer.setUnits(units);
        when(gamePlayerDao.getGamePlayerByGamePlayerId(eq(gamePlayerId))).thenReturn(gamePlayer);

        // when
        gameActionService.moveUnitsAction(moveUnitUpdate);

        // then
        verify(gamePlayerDao, times(2)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
        assertThat(gamePlayerIdArgumentCaptor.getAllValues().get(0), is(gamePlayerId));
        assertThat(gamePlayerIdArgumentCaptor.getAllValues().get(1), is(gamePlayerId));
        assertThat(unitArgumentCaptor.getAllValues().get(0).getStrength(), is(1));
        assertThat(unitArgumentCaptor.getAllValues().get(1).getStrength(), is(9));
    }

    @Test(expected = CannotMoveUnitException.class)
    public void testWhenMoveAllUnitsFromATerritory() {
        // given
        MoveUnitUpdate moveUnitUpdate = new MoveUnitUpdate();
        moveUnitUpdate.setFromTerritory(Territory.DENMARK);
        moveUnitUpdate.setToTerritiory(Territory.ICELAND);
        moveUnitUpdate.setGamePlayerId(gamePlayerId);
        Map<UnitType, Integer> unitsToMove = new HashMap<>();
        unitsToMove.put(UnitType.SOLDIER, 5);
        moveUnitUpdate.setUnitsToMove(unitsToMove);
        List<Unit> persistedUnitsFromTerritory = Arrays.asList(createSoldierUnit(5, Territory.DENMARK));
        List<Unit> persistedUnitsToTerritory = Arrays.asList(createSoldierUnit(5, Territory.ICELAND));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(eq(gamePlayerId), eq(Territory.DENMARK))).thenReturn(persistedUnitsFromTerritory);
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(eq(gamePlayerId), eq(Territory.ICELAND))).thenReturn(persistedUnitsToTerritory);

        // when
        gameActionService.moveUnitsAction(moveUnitUpdate);

        // then (exception)
    }

    @Test(expected = CannotMoveUnitException.class)
    public void testWhenMoveUnitsToNotOwnedTerritory() {
        // given
        MoveUnitUpdate moveUnitUpdate = new MoveUnitUpdate();

        // when
        gameActionService.moveUnitsAction(moveUnitUpdate);

        // then (exception)
    }

    @Test
    public void testGameActionStatusPlaceUnits() {
        ArgumentCaptor<GamePlayer> gamePlayerArgumentCaptor = ArgumentCaptor.forClass(GamePlayer.class);

        // given
        GamePlayer gamePlayer = createGamePlayer(ActionStatus.PLACE_UNITS);
        when(gamePlayerDao.getGamePlayerByGamePlayerId(eq(gamePlayerId))).thenReturn(gamePlayer);

        // when
        gameActionService.updateToNextAction(gamePlayerId);

        // then
        verify(gamePlayerDao, times(1)).updateGamePlayer(gamePlayerArgumentCaptor.capture());
        assertThat(gamePlayerArgumentCaptor.getValue().getActionStatus(), is(ActionStatus.ATTACK));
    }

    @Test
    public void testGameActionStatusAttack() {
        ArgumentCaptor<GamePlayer> gamePlayerArgumentCaptor = ArgumentCaptor.forClass(GamePlayer.class);

        // given
        GamePlayer gamePlayer = createGamePlayer(ActionStatus.ATTACK);
        when(gamePlayerDao.getGamePlayerByGamePlayerId(eq(gamePlayerId))).thenReturn(gamePlayer);

        // when
        gameActionService.updateToNextAction(gamePlayerId);

        // then
        verify(gamePlayerDao, times(1)).updateGamePlayer(gamePlayerArgumentCaptor.capture());
        assertThat(gamePlayerArgumentCaptor.getValue().getActionStatus(), is(ActionStatus.MOVE));
    }

    @Test
    public void testGameActionStatusMove() {
        ArgumentCaptor<GamePlayer> gamePlayerArgumentCaptor = ArgumentCaptor.forClass(GamePlayer.class);

        // given
        GamePlayer gamePlayer = createGamePlayer(ActionStatus.MOVE);
        GamePlayer nextGamePlayerInTurn = createGamePlayer(ActionStatus.MOVE);
        when(gamePlayerDao.getGamePlayerByGamePlayerId(any(GamePlayerId.class))).thenReturn(gamePlayer).thenReturn(nextGamePlayerInTurn);

        // when
        gameActionService.updateToNextAction(gamePlayerId);

        // then
        verify(gamePlayerDao, times(2)).updateGamePlayer(gamePlayerArgumentCaptor.capture());
        assertThat(gamePlayerArgumentCaptor.getAllValues().get(0).getActionStatus(), is(ActionStatus.PLACE_UNITS));
        assertThat(gamePlayerArgumentCaptor.getAllValues().get(0).isActivePlayerTurn(), is(true));
        assertThat(gamePlayerArgumentCaptor.getAllValues().get(1).isActivePlayerTurn(), is(false));
    }

    private Unit createSoldierUnit(int strength, Territory territory) {
        Unit unit = new Unit();
        unit.setStrength(strength);
        unit.setTerritory(territory);
        unit.setTypeOfUnit(UnitType.SOLDIER);
        return unit;
    }

    private AttackActionUpdate createDefaultAttackActionUpdate() {
        AttackActionUpdate attackActionUpdate = new AttackActionUpdate();
        attackActionUpdate.setGamePlayerId(gamePlayerId);
        attackActionUpdate.setDefendingGamePlayerId(defendingGamePlayerId);
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

    private GamePlayer createGamePlayer(ActionStatus status) {
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setActionStatus(status);
        return gamePlayer;
    }
}
