package se.freddejones.game.yakutia.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.dao.impl.PlayerDaoImpl;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerDaoExceptionTest {

    private PlayerDao playerDao;
    private SessionFactory sessionFactoryMock;

    @Before
    public void setUp() throws Exception {
        sessionFactoryMock = mock(SessionFactory.class);
        playerDao = new PlayerDaoImpl(sessionFactoryMock);
    }

    @Test(expected = PlayerAlreadyExistsException.class)
    public void testShouldThrowExceptionForAlreadyExistingPlayer() {
        // given
        Player p = new Player();

        Session sessionMock = mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        doThrow(new ConstraintViolationException("message", null, null)).when(sessionMock).saveOrUpdate(p);

        // when
        playerDao.createPlayer(p);
    }

}
