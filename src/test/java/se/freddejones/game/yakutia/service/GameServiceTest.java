package se.freddejones.game.yakutia.service;

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
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);

        // When: setting game to started
        gameService.setGameToStarted(GAME_ID);

        // Then: exception is thrown
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayersAcceptedInvite() throws Exception {

        // game playing player
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);

        // opponent
        List<GamePlayer> gamePlayers = new GamePlayersListBuilder()
                .addGamePlayer(new GamePlayerMockBuilder()
                        .setGamePlayerId(DEFENDING_GAME_PLAYER_ID)
                        .setGamePlayerStatus(GamePlayerStatus.INVITED)
                        .build())
                .addGamePlayer(gamePlayerMock)
                .build();
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);

        // When: setting game to started
        gameService.setGameToStarted(GAME_ID);

        // Then: exception is thrown
    }

    @Test(expected = TooManyPlayersException.class)
    public void testToManyPlayersToStartGame() throws Exception {
        // Given: To many players added to game players
        List<GamePlayer> gamePlayers = new ArrayList<>();
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
        } catch (TooManyPlayersException e) {
            fail("Should not enter this exception");
        } catch (CouldNotCreateGameException e) {
            verifyZeroInteractions(gameDaoMock);
        }

    }

    private void setupValidNumberOfPlayersInMock() {
        List<GamePlayer> gamePlayers = new ArrayList<>();
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);
        gamePlayers.add(gamePlayerMock);
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);
    }

    private void setupGetGamesForPlayerDefaultMockSettings() {
        when(gamePlayerDaoMock.getGamePlayersByPlayerId(anyLong())).thenReturn(
                new GamePlayersListBuilder().addGamePlayer(gamePlayerMock).build());
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(GAME_ID);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getCreationTime()).thenReturn(new Date());
        when(gameMock.getGameStatus()).thenReturn(GameStatus.CREATED);
    }

    public static class GamePlayersListBuilder {
        private List<GamePlayer> gamePlayers;

        public GamePlayersListBuilder() {
            gamePlayers = new ArrayList<>();
        }

        public GamePlayersListBuilder addGamePlayer(GamePlayer gp) {
            gamePlayers.add(gp);
            return this;
        }

        public List<GamePlayer> build() {
            return gamePlayers;
        }
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

    public static class GamePlayerMockBuilder {
        private GamePlayer gamePlayerMock;

        public GamePlayerMockBuilder() {
            gamePlayerMock = mock(GamePlayer.class);
        }

        public GamePlayerMockBuilder setGamePlayerId(Long id) {
            when(gamePlayerMock.getGamePlayerId()).thenReturn(id);
            return this;
        }

        public GamePlayerMockBuilder setUnits(List<Unit> units) {
            when(gamePlayerMock.getUnits()).thenReturn(units);
            return this;
        }

        public GamePlayerMockBuilder setGamePlayerStatus(GamePlayerStatus gamePlayerStatus) {
            when(gamePlayerMock.getGamePlayerStatus()).thenReturn(gamePlayerStatus);
            return this;
        }

        public GamePlayer build() {
            return gamePlayerMock;
        }
    }
}
