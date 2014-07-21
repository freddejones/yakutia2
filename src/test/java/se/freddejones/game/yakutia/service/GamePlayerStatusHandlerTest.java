package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.service.impl.GamePlayerStatusHandlerImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class GamePlayerStatusHandlerTest {

    private GamePlayerDao gamePlayerDao;
    private GamePlayerStatusHandler gamePlayerStatusHandler;
    private GamePlayerId gamePlayerId;
    private GamePlayer gamePlayer;

    @Before
    public void setUp() throws Exception {
        gamePlayerDao = mock(GamePlayerDao.class);
        gamePlayerStatusHandler = new GamePlayerStatusHandlerImpl(gamePlayerDao);
        gamePlayerId = new GamePlayerId(1L);
        gamePlayer = new GamePlayer();
        when(gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId)).thenReturn(gamePlayer);
    }

    @Test
    public void testAcceptInviteFetchGamePlayerId() {
        // when
        gamePlayerStatusHandler.acceptGameInvite(gamePlayerId);

        // then
        verify(gamePlayerDao, times(1)).getGamePlayerByGamePlayerId(gamePlayerId);
    }

    @Test
    public void testDeclineInviteFetchGamePlayerId() {
        // when
        gamePlayerStatusHandler.declineGameInvite(gamePlayerId);

        // then
        verify(gamePlayerDao, times(1)).getGamePlayerByGamePlayerId(gamePlayerId);
    }

    @Test
    public void testAcceptInviteUpdatesGamePlayer() {
        // when
        gamePlayerStatusHandler.acceptGameInvite(gamePlayerId);

        // then
        verify(gamePlayerDao, times(1)).updateGamePlayer(gamePlayer);
    }

    @Test
    public void testDeclineINviteUpdatesGamePlayer() {
        // when
        gamePlayerStatusHandler.declineGameInvite(gamePlayerId);

        // then
        verify(gamePlayerDao, times(1)).updateGamePlayer(gamePlayer);
    }
}