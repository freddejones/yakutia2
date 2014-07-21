package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.exception.CannotCreateGameException;
import se.freddejones.game.yakutia.exception.CannotStartGameException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.service.impl.GameServiceImpl;
import se.freddejones.game.yakutia.service.impl.GameTerritoryHandlerServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private ArrayList<PlayerId> mockArrayList;

    public static final String GAME_NAME = "gameName";
    private GameService gameService;
    private GameDao gameDaoMock;
    private GameSetupService gameSetupService;
    private GamePlayerDao gamePlayerDao;
    private List<PlayerId> enoughInvitedPlayers;
    private PlayerId playerId;

    @Before
    public void setUp() throws Exception {
        gameDaoMock = mock(GameDao.class);
        enoughInvitedPlayers = Arrays.asList(new PlayerId(2L), new PlayerId(3L));
        playerId = new PlayerId(1L);
        gamePlayerDao = mock(GamePlayerDao.class);
        gameSetupService = mock(GameSetupService.class);
        gameService = new GameServiceImpl(gameDaoMock, gamePlayerDao, gameSetupService);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateGame() {
        // given
        CreateGame createGame = new CreateGame(playerId, GAME_NAME, enoughInvitedPlayers);
        GameId gameId = new GameId(1L);
        when(gameDaoMock.createNewGame(any(CreateGame.class))).thenReturn(gameId);

        // when
        GameId receivedGameId = gameService.createNewGame(createGame);

        // then
        assertThat(receivedGameId.getGameId(), is(gameId.getGameId()));
        verify(gameDaoMock, times(1)).createNewGame(createGame);
    }

    @Test(expected = CannotCreateGameException.class)
    public void testNotEnoughPlayersToStartGame() {
        // given
        List<PlayerId> invitedPlayers = Arrays.asList(new PlayerId(2L));
        CreateGame createGame = new CreateGame(playerId, GAME_NAME, invitedPlayers);

        // when
        gameService.createNewGame(createGame);
    }

    @Test(expected = CannotCreateGameException.class)
    public void testTooManyPlayersForCreateAGame() {
        // given
        CreateGame createGameMock = mock(CreateGame.class);
        when(createGameMock.getInvitedPlayers()).thenReturn(mockArrayList);
        int size = GameTerritoryHandlerServiceImpl.getLandAreas().size();
        when(mockArrayList.size()).thenReturn(size);

        // when
        gameService.createNewGame(createGameMock);
    }

    @Test
    public void testGetGamesForPlayerId() {
        // given
        when(gamePlayerDao.getGamePlayersByPlayerId(playerId)).thenReturn(Arrays.asList(new GamePlayer()));

        // when
        List<Game> games = gameService.getGamesForPlayerById(playerId);

        // then
        assertThat(games.size(), is(1));
        verify(gamePlayerDao, times(1)).getGamePlayersByPlayerId(playerId);
    }

    @Test
    public void testSetGameToStarted() {
        // given
        TestFixture testFixture = new TestFixture();
        testFixture.setupValidGameStartScenario();
        GamePlayerId gamePlayerId = testFixture.gamePlayerId;
        GameId gameId = testFixture.gameId;

        // when
        gameService.startGame(gamePlayerId);

        // then
        verify(gameDaoMock, times(1)).startGame(gameId);
        verify(gameSetupService, times(1)).initializeNewGame(anySet());
    }

    @Test(expected = CannotStartGameException.class)
    public void testStartGameWithZeroAcceptedInvites() {
        // given
        TestFixture testFixture = new TestFixture();
        testFixture.setupValidGameStartScenario();
        GamePlayerId gamePlayerId = testFixture.gamePlayerId;
        when(testFixture.connectedGamePlayer.getGamePlayerStatus()).thenReturn(GamePlayerStatus.INVITED);

        // when
        gameService.startGame(gamePlayerId);
    }

    @Test(expected = CannotStartGameException.class)
    public void testThatItIsNotPossibleToStartGameAsNonCreator() {
        // given
        TestFixture testFixture = new TestFixture();
        testFixture.setupValidGameStartScenario();
        GamePlayerId gamePlayerId = testFixture.gamePlayerId;
        testFixture.game.setGameCreatorPlayerId(2L);

        // when
        gameService.startGame(gamePlayerId);
    }

    @Test
    public void testEndGame() {
        // given
        GameId gameId = new GameId(1L);

        // when
        gameService.endGame(gameId);

        // verify
        verify(gameDaoMock, times(1)).endGame(gameId);

    }

    class TestFixture {
        public static final long PLAYER_ID = 1L;
        public GamePlayer gamePlayer;
        public Game game;
        public GamePlayerId gamePlayerId;
        public GameId gameId;
        public GamePlayer connectedGamePlayer;

        public TestFixture() {
            gamePlayer = new GamePlayer();
            gamePlayerId = new GamePlayerId(1L);
            gamePlayer.setGamePlayerId(gamePlayerId.getGamePlayerId());
            gamePlayer.setPlayerId(PLAYER_ID);
            game = new Game();
            gameId = new GameId(1L);
            game.setGameId(gameId.getGameId());
            game.setGameCreatorPlayerId(PLAYER_ID);
            connectedGamePlayer = mock(GamePlayer.class);
        }

        public void setupValidGameStartScenario() {
            gamePlayer.setPlayerId(1L);
            gamePlayer.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
            game.setGameCreatorPlayerId(1L);
            game.setGameId(gameId.getGameId());
            gamePlayer.setGame(game);
            when(connectedGamePlayer.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);
            when(gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId)).thenReturn(gamePlayer);
            when(gamePlayerDao.getGamePlayersByGameId(gameId)).thenReturn(Arrays.asList(connectedGamePlayer, gamePlayer));
        }
    }
}