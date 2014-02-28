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
import se.freddejones.game.yakutia.model.BattleCalculator;
import se.freddejones.game.yakutia.model.BattleResult;
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

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    public static final long GAME_ID = 1L;
    public static final long PLAYER_ID = 1L;
    public static final long GAME_PLAYER_ID = 12L;
    public static final long DEFENDING_GAME_PLAYER_ID = 48L;
    public static final long DEFENDING_GAMEPLAYER_ID = 45L;
    @Mock private GamePlayerDao gamePlayerDaoMock;
    @Mock private UnitDao unitDaoMock;
    @Mock private GameDao gameDaoMock;
    @Mock private GameSetupService gameSetupServiceMock;
    @InjectMocks private GameServiceImpl gameService;

    private GamePlayer gamePlayerMock;
    private GamePlayer counterGamePlayerMock;
    private Game gameMock;
    @Before
    public void setup() {
        gamePlayerMock = mock(GamePlayer.class);
        counterGamePlayerMock = mock(GamePlayer.class);
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
        // given
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(PLAYER_ID, GAME_ID)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(gamePlayerMock);
        gamePlayers.add(counterGamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);

        when(gamePlayerMock.getUnits())
                .thenReturn(new UnitBuilder()
                        .addUnit(Territory.SWEDEN,1)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

        // when
        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(PLAYER_ID, GAME_ID);

        // then
        assertThat(territoryDTOList.size()).isEqualTo(2);
        assertThat(territoryDTOList.get(0).getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(territoryDTOList.get(1).getLandName()).isEqualTo(Territory.UNASSIGNEDLAND.toString());
    }

    @Test
    public void testThatGetTerritoryInformationBelongsToPlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(10L, 12L)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<>();

        when(gamePlayerMock.getUnits())
                .thenReturn(new UnitBuilder()
                        .addUnit(Territory.SWEDEN, 1)
                        .addUnit(Territory.UNASSIGNEDLAND, 1)
                        .build());

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
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(PLAYER_ID, GAME_ID)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();

        when(gamePlayerMock.getUnits())
                .thenReturn(new UnitBuilder()
                        .addUnit(Territory.SWEDEN,1)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

        gamePlayers.add(gamePlayerMock);
        GamePlayer counterPartGamePlayer = mock(GamePlayer.class);
        when(counterPartGamePlayer.getGamePlayerId()).thenReturn(DEFENDING_GAMEPLAYER_ID);
        when(counterPartGamePlayer.getUnits()).thenReturn(getUnitsList(Territory.FINLAND, 1));
        gamePlayers.add(counterPartGamePlayer);
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);

        // when
        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(PLAYER_ID, GAME_ID);

        // then
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
            fail("Should not enter this exception");
        } catch (ToManyPlayersException e) {
            fail("Should not enter this exception");
        } catch (CouldNotCreateGameException e) {
            verifyZeroInteractions(gameDaoMock);
        }

    }

    @Test
    public void testPlaceUnitUpdateValid() throws Exception {

        // Given
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate(3, "SWEDEN", 1L, 1L);
        setupGetGamesForPlayerDefaultMockSettings();

        when(gamePlayerMock.getUnits())
                .thenReturn(new UnitBuilder()
                        .addUnit(Territory.SWEDEN,5)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

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

        when(gamePlayerMock.getUnits())
                .thenReturn(new UnitBuilder()
                        .addUnit(Territory.SWEDEN,1)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

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
    public void testAttackTerritoryAndClaimTerritory() throws Exception {

        int attackingTerritoryStrength = 2;
        AttackActionUpdate attackActionUpdate =
                new AttackActionUpdate(Territory.SWEDEN.toString(), Territory.NORWAY.toString(), 2, GAME_ID, PLAYER_ID);

        // attacking part
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(GAME_ID, PLAYER_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.SWEDEN, attackingTerritoryStrength));
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.NORWAY, 1));

        // battle calc
        BattleCalculator battleCalculator = mock(BattleCalculator.class);
        gameService.setBattleCalculator(battleCalculator);
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(1);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(0);
        when(battleResult.isTakenOver()).thenReturn(true);

        TerritoryDTO returnObj = gameService.attackTerritoryAction(attackActionUpdate);
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
        when(gamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.SWEDEN, attackingTerritoryStrength));
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.NORWAY, 2));
        when(defendingGamePlayerMock.getGamePlayerId()).thenReturn(DEFENDING_GAME_PLAYER_ID);

        // battle calc
        BattleCalculator battleCalculator = mock(BattleCalculator.class);
        gameService.setBattleCalculator(battleCalculator);
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(1);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(1);
        when(battleResult.isTakenOver()).thenReturn(false);

        gameService.attackTerritoryAction(attackActionUpdate);

        // Then:
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(DEFENDING_GAME_PLAYER_ID), any(Unit.class));
    }

    @Test
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
        when(gamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.SWEDEN, attackingTerritoryStrength));
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.NORWAY, 1));

        // battle calc
        BattleCalculator battleCalculator = mock(BattleCalculator.class);
        gameService.setBattleCalculator(battleCalculator);
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(1);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(0);
        when(battleResult.isTakenOver()).thenReturn(false);

        TerritoryDTO returnObj = gameService.attackTerritoryAction(attackActionUpdate);

        // Then:
        assertThat(returnObj.getUnits()).isEqualTo(1);
        assertThat(returnObj.getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(returnObj.isOwnedByPlayer()).isTrue();
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
        when(gamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.SWEDEN, attackingTerritoryStrength));
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);

        // defending part
        GamePlayer defendingGamePlayerMock = mock(GamePlayer.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndTerritory(GAME_ID, Territory.NORWAY)).thenReturn(defendingGamePlayerMock);
        when(defendingGamePlayerMock.getUnits()).thenReturn(getUnitsList(Territory.NORWAY, 2));
        when(defendingGamePlayerMock.getGamePlayerId()).thenReturn(DEFENDING_GAME_PLAYER_ID);

        // battle calc
        BattleCalculator battleCalculator = mock(BattleCalculator.class);
        gameService.setBattleCalculator(battleCalculator);
        BattleResult battleResult = mock(BattleResult.class);
        when(battleCalculator.battle(any(Unit.class), any(Unit.class))).thenReturn(battleResult);
        when(battleResult.getAttackingTerritoryLosses()).thenReturn(0);
        when(battleResult.getDefendingTerritoryLosses()).thenReturn(1);
        when(battleResult.isTakenOver()).thenReturn(false);

        gameService.attackTerritoryAction(attackActionUpdate);

        // Then:
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(GAME_PLAYER_ID), any(Unit.class));
        verify(gamePlayerDaoMock, times(1)).setUnitsToGamePlayer(eq(DEFENDING_GAME_PLAYER_ID), any(Unit.class));
    }

    private List<Unit> getUnitsList(Territory territory, int strength) {
        List<Unit> units = new ArrayList<>();
        Unit u = new Unit();
        u.setTerritory(territory);
        u.setStrength(strength);
        units.add(u);
        return units;
    }

    private void addUnitToUnitsList(List<Unit> units, Territory territory, int strength) {
        Unit u = new Unit();
        u.setTerritory(territory);
        u.setStrength(strength);
        units.add(u);
    }

    private void setupValidNumberOfPlayersInMock() {
        List<GamePlayer> gamePlayers = new ArrayList<>();
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

    
    public static class UnitBuilder {
        private List<Unit> units;

        public UnitBuilder() {
            units = new ArrayList<>();
        }

        public UnitBuilder addUnit(Territory territory, int strength) {
            Unit u = new Unit();
            u.setTerritory(territory);
            u.setStrength(strength);
            units.add(u);
            return this;
        }

        public List<Unit> build() {
            return units;
        }
    }
}
