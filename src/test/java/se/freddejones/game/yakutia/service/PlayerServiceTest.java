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
}
