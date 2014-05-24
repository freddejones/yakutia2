package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.TestBoilerplate;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.exception.*;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameInviteDTO;
import se.freddejones.game.yakutia.model.dto.InvitedPlayer;
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

public class GameServiceTest {

    public static final long GAME_ID = 1L;
    public static final long PLAYER_ID = 1L;
    public static final long GAME_PLAYER_ID = 12L;
    public static final long DEFENDING_GAME_PLAYER_ID = 48L;
    public static final String GAME_NAME = "GAME_NAME";

    private GamePlayerDao gamePlayerDaoMock;
    private GameDao gameDaoMock;
    private GameSetupService gameSetupServiceMock;
    private GameService gameService;
    private GamePlayer gamePlayerMock;
    private Game gameMock;

    @Before
    public void setup() {
        gamePlayerMock = mock(GamePlayer.class);
        gameMock = mock(Game.class);
        gameDaoMock = mock(GameDao.class);
        gamePlayerDaoMock = mock(GamePlayerDao.class);
        gameSetupServiceMock = mock(GameSetupService.class);
        gameService = new GameServiceImpl(gameDaoMock, gamePlayerDaoMock, gameSetupServiceMock);
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
    public void testForExceptionIfNotEnoughInvitedPlayersWhenCreatingGame() throws Exception {
        // given
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setInvites(new ArrayList<InvitedPlayer>());
        createGameDTO.setCreatedByPlayerId(PLAYER_ID);
        createGameDTO.setGameName(GAME_NAME);

        // when
        gameService.createNewGame(createGameDTO);
    }

    @Test
    public void testCreateNewGameWithEnoughNumberOfPlayers() throws Exception {
        // given
        List<InvitedPlayer> invitedPlayers = new ArrayList<InvitedPlayer>();
        invitedPlayers.add(new InvitedPlayer());
        invitedPlayers.add(new InvitedPlayer());

        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setInvites(invitedPlayers);
        createGameDTO.setCreatedByPlayerId(PLAYER_ID);
        createGameDTO.setGameName(GAME_NAME);

        // when
        gameService.createNewGame(createGameDTO);

        // then
        verify(gameDaoMock, times(1)).createNewGame(any(CreateGameDTO.class));
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayerToStartGame() throws Exception {
        // Given: No players received from gamePlayerDao
        when(gamePlayerDaoMock.getGamePlayersByGameId(1L)).thenReturn(new ArrayList<GamePlayer>());
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getGameCreatorPlayerId()).thenReturn(PLAYER_ID);

        // When: setting game to started
        gameService.setGameToStarted(GAME_ID, PLAYER_ID);

        // Then: exception is thrown
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayerToStartGameWhenOnlyOnePlayerExists() throws Exception {
        // Given: No players received from gamePlayerDao
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getGameCreatorPlayerId()).thenReturn(PLAYER_ID);

        // When: setting game to started
        gameService.setGameToStarted(GAME_ID, PLAYER_ID);

        // Then: exception is thrown
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayersAcceptedInvite() throws Exception {

        // game playing player
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);

        // opponent
        List<GamePlayer> gamePlayers = new TestBoilerplate.GamePlayersListBuilder()
                .addGamePlayer(new TestBoilerplate.GamePlayerMockBuilder()
                        .setGamePlayerId(DEFENDING_GAME_PLAYER_ID)
                        .setGamePlayerStatus(GamePlayerStatus.INVITED)
                        .build())
                .addGamePlayer(gamePlayerMock)
                .build();
        when(gamePlayerDaoMock.getGamePlayersByGameId(GAME_ID)).thenReturn(gamePlayers);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getGameCreatorPlayerId()).thenReturn(PLAYER_ID);

        // When: setting game to started
        gameService.setGameToStarted(GAME_ID, PLAYER_ID);

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
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getGameCreatorPlayerId()).thenReturn(PLAYER_ID);

        // When: setting game to started
        gameService.setGameToStarted(GAME_ID, PLAYER_ID);

        // Then: exception is thrown
    }

    @Test
    public void testStartGameSuccessfully() throws Exception {
        setupValidNumberOfPlayersInMock();
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getGameCreatorPlayerId()).thenReturn(PLAYER_ID);

        gameService.setGameToStarted(GAME_ID, PLAYER_ID);

        verify(gameSetupServiceMock).initializeNewGame(any(List.class));
        verify(gameDaoMock).startGame(1L);
    }

    @Test
    public void testStartGameThrowsException() {
        try {
            setupValidNumberOfPlayersInMock();
            when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
            when(gameMock.getGameCreatorPlayerId()).thenReturn(PLAYER_ID);
            doThrow(CouldNotCreateGameException.class).when(gameSetupServiceMock)
                    .initializeNewGame(any(List.class));
            gameService.setGameToStarted(GAME_ID, PLAYER_ID);
        } catch (NotEnoughPlayersException e) {
            fail("Should not enter this exception");
        } catch (TooManyPlayersException e) {
            fail("Should not enter this exception");
        } catch (OnlyCreatorCanStartGame onlyCreatorCanStartGame) {
            fail("Should not enter this exception");
        } catch (CouldNotCreateGameException e) {
            verify(gameDaoMock, times(1)).getGameByGameId(GAME_ID);
        }
    }

    @Test
    public void testSetGameToFinished() throws Exception {
        // when
        gameService.setGameToFinished(GAME_ID);

        // then
        verify(gameDaoMock, times(1)).endGame(GAME_ID);
    }

    @Test
    public void testToDeclineGameInvitation() throws Exception {
        // given
        GameInviteDTO gameInviteDTO = mock(GameInviteDTO.class);
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(),anyLong())).thenReturn(gamePlayerMock);

        // when
        gameService.declineGameInvite(gameInviteDTO);

        // then
        verify(gamePlayerDaoMock, times(1)).updateGamePlayer(any(GamePlayer.class));

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
                new TestBoilerplate.GamePlayersListBuilder().addGamePlayer(gamePlayerMock).build());
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(GAME_ID);
        when(gamePlayerMock.getGamePlayerId()).thenReturn(GAME_PLAYER_ID);
        when(gameDaoMock.getGameByGameId(GAME_ID)).thenReturn(gameMock);
        when(gameMock.getCreationTime()).thenReturn(new Date());
        when(gameMock.getGameStatus()).thenReturn(GameStatus.CREATED);
    }
}
