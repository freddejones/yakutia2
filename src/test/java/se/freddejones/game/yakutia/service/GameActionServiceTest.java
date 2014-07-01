package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.TestBoilerplate;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.service.impl.GameActionServiceImpl;

import java.util.Date;

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

//    @Test
//    public void testPlaceUnitUpdateValid() throws Exception {
//
//        // Given
//        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdate();
//        setupGetGamesForPlayerDefaultMockSettings();
//
//        when(gamePlayerMock.getUnits())
//                .thenReturn(new TestBoilerplate.UnitBuilder()
//                        .addUnit(Territory.SWEDEN,5)
//                        .addUnit(Territory.UNASSIGNEDLAND,1)
//                        .build());
//
//        Unit unitMock = mock(Unit.class);
//        when(unitMock.getStrength()).thenReturn(3);
//        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);
//
//        // When: placing units
//        TerritoryDTO returnObj = gameActionService.placeUnitAction(placeUnitUpdate);
//
//        // Then
//        assertThat(returnObj.isOwnedByPlayer()).isTrue();
//        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
//        assertThat(returnObj.getUnits()).isEqualTo(8);
//    }
//
//    @Test(expected = NotEnoughUnitsException.class)
//    public void testPlaceUnitWhenInsufficientFunds() throws Exception {
//        // Given
//        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdate();
//        placeUnitUpdate.setNumberOfUnits(1);
//        setupGetGamesForPlayerDefaultMockSettings();
//        Unit unitMock = mock(Unit.class);
//        when(unitMock.getStrength()).thenReturn(0);
//        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);
//
//        // When: placing units
//        gameActionService.placeUnitAction(placeUnitUpdate);
//    }
//
//    @Test
//    public void testPlaceUnitUpdateValidPokesGamePlayerDao() throws Exception {
//        // Given
//        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdate();
//        setupGetGamesForPlayerDefaultMockSettings();
//
//        when(gamePlayerMock.getUnits())
//                .thenReturn(new TestBoilerplate.UnitBuilder()
//                        .addUnit(Territory.SWEDEN,1)
//                        .addUnit(Territory.UNASSIGNEDLAND,1)
//                        .build());
//
//        Unit unitMock = mock(Unit.class);
//        when(unitMock.getStrength()).thenReturn(3);
//        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);
//
//        // When: placing units
//        gameActionService.placeUnitAction(placeUnitUpdate);
//
//        // then
//        verify(gamePlayerDaoMock, times(2)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
//    }
//
//
//    @Test(expected = TerritoryNotConnectedException.class)
//    public void testAttackTerritoryNotConnectedTerritory() throws Exception {
//        AttackActionUpdate attackActionUpdate =
//                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.FINLAND.toString(), 5, GAME_ID, PLAYER_ID);
//
//        gameActionService.attackTerritoryAction(attackActionUpdate);
//    }


//    private void setupGetGamesForPlayerDefaultMockSettings() {
//        when(gamePlayerDaoMock.getGamePlayersByPlayerId(anyLong())).thenReturn(
//                new TestBoilerplate.GamePlayersListBuilder().addGamePlayer(gamePlayerMock).build());
//        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
//        when(gamePlayerMock.getGameId()).thenReturn(GAME_ID);
//        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
//        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
//        when(gameMock.getCreationTime()).thenReturn(new Date());
//        when(gameMock.getGameStatus()).thenReturn(GameStatus.CREATED);
//    }
//
//    private PlaceUnitUpdate getPlaceUnitUpdate() {
//        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate();
//        placeUnitUpdate.setGameId(GAME_ID);
//        placeUnitUpdate.setNumberOfUnits(3);
//        placeUnitUpdate.setPlayerId(PLAYER_ID);
//        placeUnitUpdate.setTerritory("SWEDEN");
//        return placeUnitUpdate;
//    }

}
