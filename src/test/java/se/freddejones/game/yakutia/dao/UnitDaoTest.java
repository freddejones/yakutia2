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
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.UnitId;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class UnitDaoTest {

    @Configuration
    @Import(HibernateConfigForTest.class)
    @ComponentScan(basePackages = "se.freddejones.game.yakutia.dao")
    static class TestConfiguration {}

    @Autowired
    private UnitDao unitDao;

    @BeforeClass
    public static void beforeSetup() throws Exception {
        TestdataHandler.resetAndRebuild();
    }

    @Before
    public void setUp() throws Exception {
        TestdataHandler.loadChangeSet(TestDataSets.GAME_GAMEPLAYER_PLAYERS_XML);
    }

    @Test
    public void testShouldGetUnits() {
        // given
        // test data loaded
        GamePlayerId gamePlayerId = new GamePlayerId(3L);

        // when
        List<Unit> units = unitDao.getUnitsByGamePlayerId(gamePlayerId);

        // then
        assertThat(units.size(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testShouldChangeOwnerOfUnit() {
        // given
        // test data loaded
        GamePlayerId gamePlayerId = new GamePlayerId(2L);
        UnitId unitId = new UnitId(2);

        // when
        unitDao.setGamePlayerIdForUnit(gamePlayerId, unitId);

        // then
        List<Unit> units = unitDao.getUnitsByGamePlayerId(gamePlayerId);
        assertThat(units.get(0).getGamePlayer().getGamePlayerId(), is(gamePlayerId.getGamePlayerId()));
    }

    @Test
    public void testShouldFetchAllUnitsForTerritoryAndGamePlayer() {
        // given
        // test data loaded
        GamePlayerId gamePlayerId = new GamePlayerId(1L);

        // when
        List<Unit> units = unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayerId, Territory.SWEDEN);

        // then
        assertThat(units.size(), is(2));
    }
}