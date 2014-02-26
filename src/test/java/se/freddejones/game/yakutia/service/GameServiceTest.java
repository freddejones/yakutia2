package se.freddejones.game.yakutia.service;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.*;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.service.impl.GameServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    public static final long GAME_ID = 1L;
    public static final long PLAYER_ID = 1L;
    public static final long GAME_PLAYER_ID = 12L;
    @Mock private GamePlayerDao gamePlayerDaoMock;
    @Mock private UnitDao unitDaoMock;
    @Mock private GameDao gameDaoMock;
    @Mock private GameSetupService gameSetupServiceMock;
    @InjectMocks private GameServiceImpl gameService;

    private GamePlayer gamePlayerMock;
    private Game gameMock;
    @Before
    public void setup() {
        gamePlayerMock = mock(GamePlayer.class);
        gameMock = mock(Game.class);
    }

    @Test
    public void testPlaceUnitStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with place unit status
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.PLACE_UNITS);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.PLACE_UNITS.toString());
    }

    @Test
    public void testAttackStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with attack status
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.ATTACK);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.ATTACK.toString());
    }

    @Test
    public void testMoveUnitsStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with move unit status
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.MOVE);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.MOVE.toString());

    }

    @Test
    public void testGetZeroGamesForPlayer() throws Exception {
        List<GameDTO> games = gameService.getGamesForPlayerById(1L);
        assertThat(games).isEmpty();
    }

    @Test
    public void testGetOneGameForPlayer() throws Exception {
        // Given a player has one game created
        setupGetGamesForPlayerDefaultMockSettings();

        List<GameDTO> games = gameService.getGamesForPlayerById(1L);

        assertThat(games.size()).isEqualTo(1);
        assertThat(games.get(0).getStatus()).isEqualTo(GameStatus.CREATED.toString());
    }

    @Test
    public void testIfGameCreatorCanStartGame() throws Exception {
        // Given a player has one game created
        setupGetGamesForPlayerDefaultMockSettings();
        when(gameMock.getGameCreatorPlayerId()).thenReturn(1L);

        List<GameDTO> games = gameService.getGamesForPlayerById(1L);

        assertThat(games.get(0).isCanStartGame()).isEqualTo(true);
    }

    @Test
    public void testIfNonGameCreatorCanStartGame() throws Exception {
        // Given a player has one game created
        setupGetGamesForPlayerDefaultMockSettings();
        when(gameMock.getGameCreatorPlayerId()).thenReturn(2L);

        // When: getting games for player
        List<GameDTO> games = gameService.getGamesForPlayerById(1L);

        assertThat(games.get(0).isCanStartGame()).isEqualTo(false);
    }

    @Test
    public void testGetTerritoryInformationReturnsEmpty() throws Exception {
        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(1L,1L);
        assertThat(territoryDTOList).isEmpty();
    }

    @Test
    public void testGetTerritoryInformationReturnsUnitsOnAGamePlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(10L, 12L)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        setupUnitsForGamePlayerDefaultMock();
        gamePlayers.add(gamePlayerMock);
        GamePlayer counterPartGamePlayer = mock(GamePlayer.class);
        gamePlayers.add(counterPartGamePlayer);
        when(gamePlayerDaoMock.getGamePlayersByGameId(12L)).thenReturn(gamePlayers);

        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(10L,12L);

        assertThat(territoryDTOList.size()).isEqualTo(2);
        assertThat(territoryDTOList.get(0).getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(territoryDTOList.get(1).getLandName()).isEqualTo(Territory.UNASSIGNEDLAND.toString());
    }

    @Test
    public void testThatGetTerritoryInformationBelongsToPlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(10L, 12L)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        setupUnitsForGamePlayerDefaultMock();
        gamePlayers.add(gamePlayerMock);
        GamePlayer counterPartGamePlayer = mock(GamePlayer.class);
        gamePlayers.add(counterPartGamePlayer);
        when(gamePlayerDaoMock.getGamePlayersByGameId(12L)).thenReturn(gamePlayers);

        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(10L,12L);

        assertThat(territoryDTOList.get(0).isOwnedByPlayer()).isTrue();
        assertThat(territoryDTOList.get(1).isOwnedByPlayer()).isTrue();
    }

    @Test
    public void testThatGetTerritoryInformationCanReturnUnitsNotBeloningToPlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(10L, 12L)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        setupUnitsForGamePlayerDefaultMock();
        gamePlayers.add(gamePlayerMock);
        GamePlayer counterPartGamePlayer = mock(GamePlayer.class);
        when(counterPartGamePlayer.getGamePlayerId()).thenReturn(45L);
        Unit unit = new Unit();
        unit.setTerritory(Territory.FINLAND);
        List<Unit> units = new ArrayList<Unit>();
        units.add(unit);
        when(counterPartGamePlayer.getUnits()).thenReturn(units);
        gamePlayers.add(counterPartGamePlayer);
        when(gamePlayerDaoMock.getGamePlayersByGameId(12L)).thenReturn(gamePlayers);

        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(10L,12L);
        assertThat(territoryDTOList.get(2).isOwnedByPlayer()).isFalse();
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayerToStartGame() throws Exception {
        // Given: No players received from gamePlayerDao
        when(gamePlayerDaoMock.getGamePlayersByGameId(1L)).thenReturn(new ArrayList<GamePlayer>());

        // When: setting game to started
        gameService.setGameToStarted(1L);

        // Then: exception is thrown
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayerToStartGameWhenOnlyOnePlayerExists() throws Exception {
        // Given: No players received from gamePlayerDao
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByGameId(1L)).thenReturn(gamePlayers);

        // When: setting game to started
        gameService.setGameToStarted(1L);

        // Then: exception is thrown
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayersAcceptedInvite() throws Exception {
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);
        GamePlayer gamePlayerMockNotAccepted = mock(GamePlayer.class);
        when(gamePlayerMockNotAccepted.getGamePlayerStatus()).thenReturn(GamePlayerStatus.INVITED);
        gamePlayers.add(gamePlayerMockNotAccepted);

        when(gamePlayerDaoMock.getGamePlayersByGameId(1L)).thenReturn(gamePlayers);

        // When: setting game to started
        gameService.setGameToStarted(1L);

        // Then: exception is thrown
    }

    @Test(expected = ToManyPlayersException.class)
    public void testToManyPlayersToStartGame() throws Exception {
        // Given: To many players added to gameplayers
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);
        for (int i=0; i<10; i++) {
            gamePlayers.add(gamePlayerMock);
        }
        when(gamePlayerDaoMock.getGamePlayersByGameId(1L)).thenReturn(gamePlayers);

        // When: setting game to started
        gameService.setGameToStarted(1L);

        // Then: exception is thrown
    }

    @Test
    public void testStartGameSuccessfully() throws Exception {
        setupValidNumberOfPlayersInMock();

        gameService.setGameToStarted(1L);

        verify(gameSetupServiceMock).initializeNewGame(any(List.class));
        verify(gameDaoMock).startGame(1L);
    }

    @Test
    public void testStartGameThrowsException() {
        try {
            setupValidNumberOfPlayersInMock();
            doThrow(CouldNotCreateGameException.class).when(gameSetupServiceMock)
                    .initializeNewGame(any(List.class));
            gameService.setGameToStarted(1L);
        } catch (NotEnoughPlayersException e) {
            Assert.fail("Should not enter this exception");
        } catch (ToManyPlayersException e) {
            Assert.fail("Should not enter this exception");
        } catch (CouldNotCreateGameException e) {
            verifyZeroInteractions(gameDaoMock);
        }

    }

    @Test
    public void testPlaceUnitUpdateValid() throws Exception {

        // Given
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate(3, "SWEDEN", 1L, 1L);
        setupGetGamesForPlayerDefaultMockSettings();
        setupUnitsForGamePlayerDefaultMock();
        Unit unitMock = mock(Unit.class);
        when(unitMock.getStrength()).thenReturn(3);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: placing units
        TerritoryDTO returnObj = gameService.placeUnitAction(placeUnitUpdate);

        // Then
        assertThat(returnObj.isOwnedByPlayer()).isTrue();
        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(returnObj.getUnits()).isEqualTo(8);
    }

    @Test(expected = NotEnoughUnitsException.class)
    public void testPlaceUnitWhenInsufficientFunds() throws Exception {
        // Given
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate(1, "SWEDEN", 1L, 1L);
        setupGetGamesForPlayerDefaultMockSettings();
        Unit unitMock = mock(Unit.class);
        when(unitMock.getStrength()).thenReturn(0);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: placing units
        gameService.placeUnitAction(placeUnitUpdate);
    }

    @Test
    public void testPlaceUnitUpdateValidPokesGamePlayerDao() throws Exception {
        // Given
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate(3, "SWEDEN", 1L, 1L);
        setupGetGamesForPlayerDefaultMockSettings();
        setupUnitsForGamePlayerDefaultMock();
        Unit unitMock = mock(Unit.class);
        when(unitMock.getStrength()).thenReturn(3);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: placing units
        gameService.placeUnitAction(placeUnitUpdate);
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(anyLong(), any(Unit.class));
    }

    @Test(expected = TerritoryNotConnectedException.class)
    public void testAttackTerritoryNotConnectedTerritory() throws Exception {
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.FINLAND.toString(), 5, GAME_ID, PLAYER_ID);

        gameService.attackTerritoryAction(attackActionUpdate);
    }

    @Test
    @Ignore
    public void testAttackTerritoryAndClaimTerritory() throws Exception {

        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.NORWAY.toString(), 5, GAME_ID, PLAYER_ID);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(GAME_ID, PLAYER_ID)).thenReturn(gamePlayerMock);
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        List<Unit> units = new ArrayList<Unit>();
        Unit u = new Unit();
        u.setStrength(6);
        u.setTerritory(Territory.SWEDEN);
        units.add(u);
        when(gamePlayerMock.getUnits()).thenReturn(units);


        TerritoryDTO returnObj = gameService.attackTerritoryAction(attackActionUpdate);
        // Then:
        assertThat(returnObj.getUnits()).isEqualTo(1);
        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(returnObj.isOwnedByPlayer()).isTrue();
        verify(gamePlayerDaoMock, times(2)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(11L, any(Unit.class));
    }

    @Test
    @Ignore
    public void testAttackTerritoryAndLooseBattleAndAllUnits() throws Exception {
        int attackingTerritoryStrength = 6;
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(),
                        Territory.NORWAY.toString(), attackingTerritoryStrength-1, GAME_ID, PLAYER_ID);

        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(GAME_ID, PLAYER_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        List<Unit> units = new ArrayList<Unit>();
        Unit u = new Unit();
        u.setStrength(6);
        u.setTerritory(Territory.SWEDEN);
        units.add(u);
        when(gamePlayerMock.getUnits()).thenReturn(units);

        TerritoryDTO returnObj = gameService.attackTerritoryAction(attackActionUpdate);

        // Then:
        assertThat(returnObj.getUnits()).isEqualTo(1);
        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(returnObj.isOwnedByPlayer()).isTrue();
        verify(gamePlayerDaoMock).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
    }

    @Test
    public void testAttackTerritoryAndTakeOutSomeUnitsFromOtherPlayer() throws Exception {

    }

    private void setupValidNumberOfPlayersInMock() {
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);
        gamePlayers.add(gamePlayerMock);
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);
    }

    private void setupGetGamesForPlayerDefaultMockSettings() {
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByPlayerId(anyLong())).thenReturn(gamePlayers);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(666L);
        when(gameDaoMock.getGameByGameId(666L)).thenReturn(gameMock);
        when(gameMock.getCreationTime()).thenReturn(new Date());
        when(gameMock.getGameStatus()).thenReturn(GameStatus.CREATED);
    }

    private void setupUnitsForGamePlayerDefaultMock() {
        setupUnitsForGamePlayerMock(5, 3);
    }

    private void setupUnitsForGamePlayerMock(int unitsForLandArea, int unassignedUnits) {
        List<Unit> unitsForGamePlayer = new ArrayList<Unit>();
        Unit unit = new Unit();
        unit.setTerritory(Territory.SWEDEN);
        unit.setStrength(unitsForLandArea);
        unitsForGamePlayer.add(unit);

        Unit unitUnassigned = new Unit();
        unitUnassigned.setTerritory(Territory.UNASSIGNEDLAND);
        unitUnassigned.setStrength(unassignedUnits);
        unitsForGamePlayer.add(unitUnassigned);
        when(gamePlayerMock.getUnits()).thenReturn(unitsForGamePlayer);
    }
}
