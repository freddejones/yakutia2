package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import se.freddejones.game.yakutia.TestBoilerplate;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.BattleResult;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.service.impl.GameActionServiceImpl;

import java.util.Date;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GameActionServiceTest {

    public static final long GAME_ID = 1L;
    public static final long PLAYER_ID = 1L;
    public static final long GAME_PLAYER_ID = 12L;
    public static final long DEFENDING_GAME_PLAYER_ID = 48L;

    private GamePlayerDao gamePlayerDaoMock;

    private GameDao gameDaoMock;
    private GamePlayer gamePlayerMock;
    private Game gameMock;
    private GameActionService gameActionService;
    private BattleCalculator battleCalculator;

    @Before
    public void setup() {
        gamePlayerMock = mock(GamePlayer.class);
        gameMock = mock(Game.class);
        gameDaoMock = mock(GameDao.class);
        gamePlayerDaoMock = mock(GamePlayerDao.class);
        battleCalculator = mock(BattleCalculator.class);
        gameActionService = new GameActionServiceImpl(gamePlayerDaoMock, battleCalculator);
    }

    @Test
    public void testPlaceUnitUpdateValid() throws Exception {

        // Given
        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdate();
        setupGetGamesForPlayerDefaultMockSettings();

        when(gamePlayerMock.getUnits())
                .thenReturn(new TestBoilerplate.UnitBuilder()
                        .addUnit(Territory.SWEDEN,5)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

        Unit unitMock = mock(Unit.class);
        when(unitMock.getStrength()).thenReturn(3);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: placing units
        TerritoryDTO returnObj = gameActionService.placeUnitAction(placeUnitUpdate);

        // Then
        assertThat(returnObj.isOwnedByPlayer()).isTrue();
        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(returnObj.getUnits()).isEqualTo(8);
    }

    @Test
    @Ignore
    public void testPlaceUnitTransitionToAttackState() throws Exception {
        fail("not implmented test yet");
    }

    @Test(expected = NotEnoughUnitsException.class)
    public void testPlaceUnitWhenInsufficientFunds() throws Exception {
        // Given
        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdate();
        placeUnitUpdate.setNumberOfUnits(1);
        setupGetGamesForPlayerDefaultMockSettings();
        Unit unitMock = mock(Unit.class);
        when(unitMock.getStrength()).thenReturn(0);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: placing units
        gameActionService.placeUnitAction(placeUnitUpdate);
    }

    @Test
    public void testPlaceUnitUpdateValidPokesGamePlayerDao() throws Exception {
        // Given
        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdate();
        setupGetGamesForPlayerDefaultMockSettings();

        when(gamePlayerMock.getUnits())
                .thenReturn(new TestBoilerplate.UnitBuilder()
                        .addUnit(Territory.SWEDEN,1)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

        Unit unitMock = mock(Unit.class);
        when(unitMock.getStrength()).thenReturn(3);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: placing units
        gameActionService.placeUnitAction(placeUnitUpdate);

        // then
        verify(gamePlayerDaoMock, times(2)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
    }

    @Test(expected = TerritoryNotConnectedException.class)
    public void testAttackTerritoryNotConnectedTerritory() throws Exception {
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.FINLAND.toString(), 5, GAME_ID, PLAYER_ID);

        gameActionService.attackTerritoryAction(attackActionUpdate);
    }

    @Test
    public void testAttackTerritoryAndClaimTerritory() throws Exception {

        int attackingTerritoryStrength = 2;
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.NORWAY.toString(), 2, GAME_ID, PLAYER_ID);

        // attacking part
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(GAME_ID, PLAYER_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                addUnit(Territory.SWEDEN, attackingTerritoryStrength)
                .build());
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                addUnit(Territory.NORWAY, 1)
                .build());

        // battle calc
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(1);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(0);
        when(battleResult.isTakenOver()).thenReturn(true);

        TerritoryDTO returnObj = gameActionService.attackTerritoryAction(attackActionUpdate);
        // Then:
        assertThat(returnObj.getUnits()).isEqualTo(1);
        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(returnObj.isOwnedByPlayer()).isTrue();
        verify(gamePlayerDaoMock, times(2)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
    }

    @Test
    public void testAttackTerritoryAndEqualLosses() throws Exception {
        int attackingTerritoryStrength = 2;
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.NORWAY.toString(), 2, GAME_ID, PLAYER_ID);

        // attacking part
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(GAME_ID, PLAYER_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                        addUnit(Territory.SWEDEN, attackingTerritoryStrength)
                        .build()
        );
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                addUnit(Territory.NORWAY, 2)
                .build());
        when(defendingGamePlayerMock.getGamePlayerId()).thenReturn(DEFENDING_GAME_PLAYER_ID);

        // battle calc
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(1);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(1);
        when(battleResult.isTakenOver()).thenReturn(false);

        gameActionService.attackTerritoryAction(attackActionUpdate);

        // Then:
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(DEFENDING_GAME_PLAYER_ID), any(Unit.class));
    }

    @Test
    @Ignore
    public void testAttackTerritoryAndLooseBattleAndAllUnits() throws Exception {
        // given
        int attackingTerritoryStrength = 2;
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(),
                        Territory.NORWAY.toString(), attackingTerritoryStrength-1, GAME_ID, PLAYER_ID);

        // attacking part
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(GAME_ID, PLAYER_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                addUnit(Territory.SWEDEN, attackingTerritoryStrength)
                .build());
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                        addUnit(Territory.NORWAY, 2)
                        .build()
        );

        // battle calc
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(1);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(0);
        when(battleResult.isTakenOver()).thenReturn(false);

        TerritoryDTO returnObj = gameActionService.attackTerritoryAction(attackActionUpdate);

        // Then:
        assertThat(returnObj.getUnits()).isEqualTo(1);
        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(returnObj.isOwnedByPlayer()).isFalse();
        verify(gamePlayerDaoMock).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
        verify(gamePlayerDaoMock, times(2)).setUnitsToGamePlayer(anyLong(), any(Unit.class));
    }

    @Test
    public void testAttackTerritoryAndTakeOutSomeUnitsFromOtherPlayer() throws Exception {
        int attackingTerritoryStrength = 2;
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.NORWAY.toString(), 2, GAME_ID, PLAYER_ID);

        // attacking part
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(GAME_ID, PLAYER_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                addUnit(Territory.SWEDEN, attackingTerritoryStrength)
                .build());
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(new TestBoilerplate.UnitBuilder().
                addUnit(Territory.NORWAY, 2)
                .build());
        when(defendingGamePlayerMock.getGamePlayerId()).thenReturn(DEFENDING_GAME_PLAYER_ID);

        // battle calc
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(0);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(1);
        when(battleResult.isTakenOver()).thenReturn(false);

        gameActionService.attackTerritoryAction(attackActionUpdate);

        // Then:
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(DEFENDING_GAME_PLAYER_ID), any(Unit.class));
    }

    private void setupGetGamesForPlayerDefaultMockSettings() {
        when(gamePlayerDaoMock.getGamePlayersByPlayerId(anyLong())).thenReturn(
                new TestBoilerplate.GamePlayersListBuilder().addGamePlayer(gamePlayerMock).build());
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(GAME_ID);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getCreationTime()).thenReturn(new Date());
        when(gameMock.getGameStatus()).thenReturn(GameStatus.CREATED);
    }

    private PlaceUnitUpdate getPlaceUnitUpdate() {
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate();
        placeUnitUpdate.setGameId(GAME_ID);
        placeUnitUpdate.setNumberOfUnits(3);
        placeUnitUpdate.setPlayerId(PLAYER_ID);
        placeUnitUpdate.setTerritory("SWEDEN");
        return placeUnitUpdate;
    }

}
