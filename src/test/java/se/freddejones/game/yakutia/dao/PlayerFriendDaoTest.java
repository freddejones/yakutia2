package se.freddejones.game.yakutia.dao;

import org.junit.Before;
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
import se.freddejones.game.yakutia.TestDataSets;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.PlayerFriendId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class PlayerFriendDaoTest {

    @Configuration
    @Import(HibernateConfig.class)
    @ComponentScan(basePackages = "se.freddejones.game.yakutia.dao")
    static class TestConfiguration {}

    @Autowired
    private PlayerFriendDao playerFriendDao;
    @Autowired
    private PlayerDao playerDao;

    @BeforeClass
    public static void beforeSetup() throws Exception {
        TestdataHandler.resetAndRebuild();
    }

    @Before
    public void setUp() throws Exception {
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);
    }

    @Test
    public void testShouldGetPlayerFriend() {
        // given
        // test data loaded
        PlayerId playerId = new PlayerId(1L);
        PlayerId playerIdFriend = new PlayerId(3L);

        // when
        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(playerId, playerIdFriend);

        // then
        assertThat(playerFriend.getPlayerFriendId(), is(notNullValue()));
    }

    @Test
    public void testShouldGetPlayerFriendById() {
        // given
        // test data loaded
        PlayerFriendId playerFriendId = new PlayerFriendId(1L);

        // when
        PlayerFriend playerFriend = playerFriendDao.getPlayerFriendById(playerFriendId);

        // then
        assertThat(playerFriend.getPlayerFriendId(), is(notNullValue()));
    }

    @Test
    public void testShouldPersistlayerFriend() {
        // given
        // test data loaded
        PlayerId playerId = new PlayerId(1L);
        Player p = playerDao.getPlayerById(playerId);
        PlayerId playerIdFriend = new PlayerId(4L);
        Player f = playerDao.getPlayerById(playerIdFriend);

        PlayerFriend playerFriend = new PlayerFriend();
        playerFriend.setPlayer(p);
        playerFriend.setFriendStatus(FriendStatus.INVITED);
        playerFriend.setFriend(f);

        // when
        PlayerFriendId playerFriendId = playerFriendDao.persistPlayerFriendEntity(playerFriend);

        // then
        PlayerFriend persistedPlayerFriend = playerFriendDao.getPlayerFriend(playerId, playerIdFriend);
        assertThat(persistedPlayerFriend.getPlayerFriendId(), is(playerFriendId.getPlayerFriendId()));
        assertThat(persistedPlayerFriend.getPlayer().getPlayerId(), is(playerId.getPlayerId()));
        assertThat(persistedPlayerFriend.getFriend().getPlayerId(), is(playerIdFriend.getPlayerId()));
    }

    @Test
    public void testShouldMergePlayerFriend() throws SQLException {
        // given
        // test data loaded
        PlayerId playerId = new PlayerId(1L);
        PlayerId playerIdFriend = new PlayerId(3L);

        // when
        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(playerId, playerIdFriend);
        playerFriend.setFriendStatus(FriendStatus.ACCEPTED);

        // when
        playerFriendDao.mergePlayerFriendEntity(playerFriend);

        // then
        PlayerFriend persistedPlayerFriend = playerFriendDao.getPlayerFriendById(new PlayerFriendId(playerFriend.getPlayerFriendId()));
        assertThat(persistedPlayerFriend.getFriendStatus(), is(FriendStatus.ACCEPTED));
    }

    @Test
    public void testShouldDeletePlayerFriend() throws SQLException {
        // given
        // test data loaded
        PlayerId playerId = new PlayerId(1L);
        PlayerId playerIdFriend = new PlayerId(3L);

        // when
        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(playerId, playerIdFriend);

        // when
        playerFriendDao.deletePlayerFriend(playerFriend);

        // then
        PlayerFriend persistedPlayerFriend = playerFriendDao.getPlayerFriendById(new PlayerFriendId(playerFriend.getPlayerFriendId()));
        assertThat(persistedPlayerFriend, is(nullValue()));
    }
}