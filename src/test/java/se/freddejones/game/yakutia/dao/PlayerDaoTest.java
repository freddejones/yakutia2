package se.freddejones.game.yakutia.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.HibernateConfig;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class PlayerDaoTest {

    @Configuration
    @Import(HibernateConfig.class)
    @ComponentScan(basePackages = {"se.freddejones.game.yakutia.dao"})
    static class TestConfiguration {}

    @Autowired
    private PlayerDao playerDao;

    @BeforeClass
    public static void setUp() throws Exception {
        TestdataHandler.resetAndRebuild();
        TestdataHandler.loadChangeSet("src/test/resources/db/testdata/players.xml");
    }

    @Test
    public void testToPersistPlayer() throws Exception {
        // given
        Player p = new Player();
        p.setName("tomte");
        p.setEmail("tomte@tomte");

        // when
        PlayerId playerId = playerDao.createPlayer(p);

        // then
        assertThat(playerId, is(notNullValue()));
    }

    @Test
    public void testGetPlayerById() throws Exception {
        // given
        // test data loaded

        // when
        long playerId = 1L;
        Player p = playerDao.getPlayerById(new PlayerId(playerId));

        // then
        assertThat(p.getPlayerId(), is(playerId));
    }

    @Test
    public void testGetAllPlayers() throws Exception {
        // given
        // test data loaded

        // when
        List<Player> players = playerDao.getAllPlayers();

        // then
        assertThat(players.size(), is(greaterThan(1)));
    }

    @Test
    public void testGetPlayerByEmail() throws Exception {
        // given
        // test data loaded

        // when
        String email = "boris@gmail.com";
        Player player = playerDao.getPlayerByEmail(email);

        // then
        assertThat(player.getEmail(), is(email));
    }

    @Test
    public void testUpdatePlayerName() throws Exception {
        // given
        // test data loaded
        PlayerId playerId = new PlayerId(1L);

        // when
        String newName = "newName";
        PlayerId playerIdUpdated = playerDao.updatePlayerName(newName, playerId);

        // then
        assertThat(playerIdUpdated.getPlayerId(), is(playerId.getPlayerId()));
    }
}

