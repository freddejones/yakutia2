package se.freddejones.game.yakutia.service;


import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.service.impl.PlayerServiceImpl;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    private PlayerService playerService;
    private PlayerDao playerDaoMock;

    @Before
    public void setUp() throws Exception {
        playerDaoMock = mock(PlayerDao.class);
        playerService = new PlayerServiceImpl(playerDaoMock);
    }

    @Test
    public void testShouldReturnPlayerIdForCreatedPlayer() {
        // given
        Player p = new Player();
        PlayerId expectedPlayerId = new PlayerId(1L);
        when(playerDaoMock.createPlayer(p)).thenReturn(expectedPlayerId);

        // when
        PlayerId playerId = playerService.createNewPlayer(p);

        // then
        assertThat(playerId.getPlayerId(), is(expectedPlayerId.getPlayerId()));
        verify(playerDaoMock, times(1)).createPlayer(p);
    }

    @Test
    public void testShouldFetchAllPlayersFromDao() {
        // given
        when(playerDaoMock.getAllPlayers()).thenReturn(new ArrayList<Player>());

        // when
        playerService.getAllPlayers();

        // then
        verify(playerDaoMock, times(1)).getAllPlayers();
    }

    @Test
    public void testShouldCreatePlayerByCallingPlayerDao() {
        // given
        Player p = new Player();
        p.setName("test");
        PlayerId createdPlayerId = new PlayerId(1L);
        when(playerDaoMock.createPlayer(p)).thenReturn(createdPlayerId);

        // when
        PlayerId playerId = playerService.createNewPlayer(p);

        // then
        assertThat(playerId.getPlayerId(), is(createdPlayerId.getPlayerId()));
        verify(playerDaoMock, times(1)).createPlayer(p);
    }

    @Test
    public void testCheckForPlayerIsFullyCreatedWhenPlayerIsFullyCreated() {
        // given
        PlayerId playerId = new PlayerId(1L);
        Player fullyCreatedPlayer = new Player();
        fullyCreatedPlayer.setName("my name is");
        when(playerDaoMock.getPlayerById(playerId)).thenReturn(fullyCreatedPlayer);

        // when
        boolean isFullyCreated = playerService.isPlayerFullyCreated(playerId);

        // then
        assertThat(isFullyCreated, is(true));
        verify(playerDaoMock, times(1)).getPlayerById(playerId);
    }

    @Test
    public void testCheckForPlayerIsFullyCreatedWhenPlayerIsNotFullyCreated() {
        // given
        PlayerId playerId = new PlayerId(1L);
        Player notFullyCreatedPlayer = new Player();
        notFullyCreatedPlayer.setName(null);
        when(playerDaoMock.getPlayerById(playerId)).thenReturn(notFullyCreatedPlayer);

        // when
        boolean isFullyCreated = playerService.isPlayerFullyCreated(playerId);

        // then
        assertThat(isFullyCreated, is(false));
        verify(playerDaoMock, times(1)).getPlayerById(playerId);
    }

    @Test
    public void testGetPlayerById() {
        // given
        PlayerId playerId = new PlayerId(1L);
        Player p = getAnyPlayer();
        when(playerDaoMock.getPlayerById(playerId)).thenReturn(p);

        // when
        Player player = playerService.getPlayerById(playerId);

        // then
        assertThat(player.getPlayerId(), is(p.getPlayerId()));
        verify(playerDaoMock, times(1)).getPlayerById(any(PlayerId.class));
    }

    @Test
    public void testGetPlayerByEmail() {
        // given
        String email = "epost@epost.se";
        Player p = getAnyPlayer();
        when(playerDaoMock.getPlayerByEmail(email)).thenReturn(p);

        // when
        Player player = playerService.getPlayerByEmail(email);

        // then
        assertThat(player.getPlayerId(), is(p.getPlayerId()));
        verify(playerDaoMock, times(1)).getPlayerByEmail(anyString());
    }

    @Test
    public void testUpdatePlayerNameGoesViaPlayerDao() {
        // given
        String newName = "newname";
        PlayerId playerId = new PlayerId(1L);
        when(playerDaoMock.updatePlayerName(newName, playerId)).thenReturn(playerId);

        // when
        PlayerId updatedPlayerId = playerService.updatePlayerName(playerId, newName);

        // then
        assertThat(updatedPlayerId.getPlayerId(), is(playerId.getPlayerId()));
        verify(playerDaoMock, times(1)).updatePlayerName(eq(newName), any(PlayerId.class));
    }

    private Player getAnyPlayer() {
        Player p = new Player();
        p.setName("tomte");
        p.setPlayerId(1L);
        p.setEmail("epost@epost.se");
        return p;
    }

}
