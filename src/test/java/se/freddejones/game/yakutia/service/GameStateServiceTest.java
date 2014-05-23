package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.freddejones.game.yakutia.TestBoilerplate;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.service.impl.GameStateServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static se.freddejones.game.yakutia.TestBoilerplate.atLeastOneNotOwnedByPlayer;

public class GameStateServiceTest {

    public static final long GAME_ID = 1L;
    public static final long PLAYER_ID = 1L;
    public static final long GAME_PLAYER_ID = 12L;
    public static final long DEFENDING_GAME_PLAYER_ID = 48L;

    private GameStateService gameStateService;
    private GameService gameService;
    private GamePlayerDao gamePlayerDaoMock;
    private GamePlayer gamePlayerMock;
    private Game gameMock;
    private GameDao gameDaoMock;

    @Before
    public void setup() {
        gameService = mock(GameService.class);
        gamePlayerDaoMock = mock(GamePlayerDao.class);
        gameStateService = new GameStateServiceImpl(gamePlayerDaoMock, gameService);

        gamePlayerMock = mock(GamePlayer.class);
        gameMock = mock(Game.class);
        gameDaoMock = mock(GameDao.class);
    }

    @Test
    public void testInitialSetupOfGameStart() throws Exception {
        // Given gameplayer object with place unit status
        setupGetGamesForPlayerDefaultMockSettings();
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(PLAYER_ID, GAME_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(null);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameStateService.getGameStateModel(GAME_ID, PLAYER_ID);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.PLACE_UNITS.toString());
        verify(gamePlayerDaoMock).setActionStatus(GAME_PLAYER_ID, ActionStatus.PLACE_UNITS);
    }

    @Test
    public void testPlaceUnitStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with place unit status
        setupGetGamesForPlayerDefaultMockSettings();
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.PLACE_UNITS);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameStateService.getGameStateModel(GAME_ID, PLAYER_ID);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.PLACE_UNITS.toString());
        verify(gamePlayerDaoMock, never()).setActionStatus(anyLong(), any(ActionStatus.class));
    }

    @Test
    public void testAttackStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with attack status
        setupGetGamesForPlayerDefaultMockSettings();
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.ATTACK);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameStateService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.ATTACK.toString());
        verify(gamePlayerDaoMock, never()).setActionStatus(anyLong(), any(ActionStatus.class));
    }

    @Test
    public void testMoveUnitsStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with move unit status
        setupGetGamesForPlayerDefaultMockSettings();
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.MOVE);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameStateService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.MOVE.toString());
        verify(gamePlayerDaoMock, never()).setActionStatus(anyLong(), any(ActionStatus.class));
    }

    @Test
    public void testGetTerritoryInformationReturnsEmpty() throws Exception {
        List<TerritoryDTO> territoryDTOList = gameStateService.getTerritoryInformationForActiveGame(1L,1L);
        assertThat(territoryDTOList).isEmpty();
    }

    @Test
    public void testThatGetTerritoryInformationCanReturnUnitsNotBeloningToPlayer() throws Exception {
        // game playing player
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(PLAYER_ID, GAME_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getUnits())
                .thenReturn(new TestBoilerplate.UnitBuilder()
                        .addUnit(Territory.SWEDEN,1)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

        // opponent player
        List<GamePlayer> gps = new TestBoilerplate.GamePlayersListBuilder()
                .addGamePlayer(new TestBoilerplate.GamePlayerMockBuilder()
                        .setGamePlayerId(DEFENDING_GAME_PLAYER_ID)
                        .setUnits(new TestBoilerplate.UnitBuilder().addUnit(Territory.FINLAND, 5).build())
                        .build())
                .addGamePlayer(gamePlayerMock)
                .build();
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gps);

        // when
        List<TerritoryDTO> territoryDTOList = gameStateService.getTerritoryInformationForActiveGame(PLAYER_ID, GAME_ID);

        // then
        assertThat(atLeastOneNotOwnedByPlayer(territoryDTOList)).isTrue();
    }

    @Test
    public void testGetTerritoryInformationReturnsUnitsOnAGamePlayer() throws Exception {
        // given
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(PLAYER_ID, GAME_ID)).thenReturn(gamePlayerMock);

        // current game player
        when(gamePlayerMock.getUnits())
                .thenReturn(new TestBoilerplate.UnitBuilder()
                        .addUnit(Territory.SWEDEN,1)
                        .addUnit(Territory.UNASSIGNEDLAND,1)
                        .build());

        List<GamePlayer> gamePlayers = new TestBoilerplate.GamePlayersListBuilder()
                .addGamePlayer(new TestBoilerplate.GamePlayerMockBuilder()
                        .setGamePlayerId(DEFENDING_GAME_PLAYER_ID)
                        .build())
                .addGamePlayer(gamePlayerMock)
                .build();
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);

        // when
        List<TerritoryDTO> territoryDTOList = gameStateService.getTerritoryInformationForActiveGame(PLAYER_ID, GAME_ID);

        // then
        assertThat(territoryDTOList.size()).isEqualTo(2);
        assertThat(territoryDTOList.get(0).getLandName()).isEqualTo(Territory.SWEDEN.toString());
        assertThat(territoryDTOList.get(1).getLandName()).isEqualTo(Territory.UNASSIGNEDLAND.toString());
    }

    @Test
    public void testThatGetTerritoryInformationBelongsToPlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(PLAYER_ID, GAME_ID)).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getUnits())
                .thenReturn(new TestBoilerplate.UnitBuilder()
                        .addUnit(Territory.SWEDEN, 1)
                        .addUnit(Territory.UNASSIGNEDLAND, 1)
                        .build());

        List<GamePlayer> gamePlayers = new TestBoilerplate.GamePlayersListBuilder()
                .addGamePlayer(new TestBoilerplate.GamePlayerMockBuilder()
                        .setGamePlayerId(DEFENDING_GAME_PLAYER_ID)
                        .build())
                .addGamePlayer(gamePlayerMock)
                .build();

        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);

        List<TerritoryDTO> territoryDTOList = gameStateService.getTerritoryInformationForActiveGame(PLAYER_ID,GAME_ID);

        assertThat(territoryDTOList.get(0).isOwnedByPlayer()).isTrue();
        assertThat(territoryDTOList.get(1).isOwnedByPlayer()).isTrue();
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


}
