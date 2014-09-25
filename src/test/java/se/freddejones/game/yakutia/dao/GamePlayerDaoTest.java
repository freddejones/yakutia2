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
import se.freddejones.game.yakutia.HibernateConfigForTest;
import se.freddejones.game.yakutia.TestDataSets;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class GamePlayerDaoTest {

    @Configuration
    @Import(HibernateConfigForTest.class)
    @ComponentScan(basePackages = {"se.freddejones.game.yakutia.dao"})
    static class TestConfiguration {}

    @Autowired
    private GamePlayerDao gamePlayerDao;

    @BeforeClass
    public static void beforeSetup() throws Exception {
        TestdataHandler.resetAndRebuild();
    }

    @Before
    public void setUp() throws Exception {
        TestdataHandler.loadChangeSet(TestDataSets.GAME_GAMEPLAYER_PLAYERS_XML);
    }

    @Test
    public void testGetGamePlayersByPlayerId() {
        // given
        // testdata loaded
        PlayerId playerId = new PlayerId(1L);

        // when
        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByPlayerId(playerId);

        // then
        assertThat(gamePlayers.size(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetGAmePlayersByGameId() {
        // given
        // testdata loaded
        GameId gameId = new GameId(1L);

        // when
        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);

        // then
        assertThat(gamePlayers.size(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetGamePlayerByGameIdAndPlayerId() {
        // given
        // testdata loaded
        PlayerId playerId = new PlayerId(1L);
        GameId gameId = new GameId(1L);

        // when
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);

        // then
        assertThat(gamePlayer.getPlayerId(), is(playerId.getPlayerId()));
        assertThat(gamePlayer.getGameId(), is(gameId.getGameId()));
    }

    @Test
    public void testGetGamePlayerByGamePlayerId() {
        // given
        // testdata loaded
        GamePlayerId gamePlayerId = new GamePlayerId(1L);

        // when
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);

        // then
        assertThat(gamePlayer.getGamePlayerId(), is(gamePlayerId.getGamePlayerId()));
    }

    @Test
    public void testSetUnitsToGamePlayer() throws Exception {
        // given
        // test data loaded
        GamePlayerId gamePlayerId = new GamePlayerId(1L);
        Unit u = new Unit();
        u.setStrength(1);
        u.setTerritory(Territory.SWEDEN);
        u.setTypeOfUnit(UnitType.TANK);

        // when
        gamePlayerDao.updateUnitsToGamePlayer(gamePlayerId, u);

        // then
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        assertThat(gamePlayer.getUnits().get(0).getGamePlayer().getGamePlayerId(), is(gamePlayerId.getGamePlayerId()));
        assertThat(gamePlayer.getUnits().get(0).getTerritory(), is(Territory.SWEDEN));
    }

    @Test
    public void testGetUnassignedLandFromGamePlayer() {
        // given
        // test data loaded
        GamePlayerId gamePlayerId = new GamePlayerId(3L);

        // when
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        Unit unassignedUnit = gamePlayer.getUnitByTerritory(Territory.UNASSIGNED_TERRITORY);

        assertThat(unassignedUnit.getGamePlayer().getGamePlayerId(), is (gamePlayerId.getGamePlayerId()));
        assertThat(unassignedUnit.getTerritory(), is(Territory.UNASSIGNED_TERRITORY));
    }

    @Test
    public void testSetActionStatusOnGamePlayer() throws Exception {
        // given
        // test data loaded
        GamePlayerId gamePlayerId = new GamePlayerId(3L);

        // when
        gamePlayerDao.setActionStatus(gamePlayerId, ActionStatus.PLACE_UNITS);

        // then
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        assertThat(gamePlayer.getActionStatus(), is(ActionStatus.PLACE_UNITS));
    }

    @Test
    public void testUpdateGamePlayer() {
        // given
        // test data loaded
        GamePlayerId gamePlayerId = new GamePlayerId(2L);
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        gamePlayer.setGamePlayerStatus(GamePlayerStatus.ALIVE);
        gamePlayer.setUnits(new ArrayList<Unit>());
        gamePlayer.setActionStatus(ActionStatus.MOVE);

        // when
        gamePlayerDao.updateGamePlayer(gamePlayer);

        // then
        GamePlayer updatedGamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        assertThat(updatedGamePlayer.getGamePlayerStatus(), is(GamePlayerStatus.ALIVE));
        assertThat(updatedGamePlayer.getUnits().size(), is(0));
        assertThat(updatedGamePlayer.getActionStatus(), is(ActionStatus.MOVE));
    }
}
