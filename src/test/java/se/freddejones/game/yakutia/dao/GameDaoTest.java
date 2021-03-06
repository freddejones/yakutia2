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
import se.freddejones.game.yakutia.HibernateConfigForTest;
import se.freddejones.game.yakutia.TestDataSets;
import se.freddejones.game.yakutia.dao.impl.GameDaoImpl;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.sql.SQLException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class GameDaoTest {

    @Configuration
    @Import(HibernateConfigForTest.class)
    @ComponentScan(basePackages = {"se.freddejones.game.yakutia.dao"})
    static class TestConfiguration {}

    @BeforeClass
    public static void setup() throws Exception {
        TestdataHandler.resetAndRebuild();
        TestdataHandler.loadChangeSet(TestDataSets.GAME_GAMEPLAYER_PLAYERS_XML);
    }

    @Autowired
    private GameDaoImpl gameDao;

    @Test
    @Transactional
    public void testShouldCreateGame() throws SQLException, ClassNotFoundException {
        // given
        // test data loaded
        CreateGame createGameDto =
                new CreateGame(new PlayerId(1L),"new game", Arrays.asList(new PlayerId(1L)));

        // when
        GameId gameId = gameDao.createNewGame(createGameDto);

        // then
        assertThat(gameId.getGameId(), is(notNullValue()));
    }

    @Test
    public void testShouldFetchGameById() throws SQLException {
        // given
        // test data loaded
        GameId gameId = new GameId(1L);

        // when
        Game game = gameDao.getGameByGameId(gameId);

        // then
        assertThat(game.getGameId(), is(gameId.getGameId()));
    }

    @Test
    public void testShouldSetGameStatusToStarted() {
        // given
        // test data loaded
        GameId gameId = new GameId(1L);

        // when
        gameDao.startGame(gameId);

        // then
        Game game = gameDao.getGameByGameId(gameId);
        assertThat(game.getGameStatus(), is(GameStatus.ONGOING));
    }

    @Test
    public void testShouldSetGameStatusToFinished() {
        // given
        // test data loaded
        GameId gameId = new GameId(1L);

        // when
        gameDao.endGame(gameId);

        // then
        Game game = gameDao.getGameByGameId(gameId);
        assertThat(game.getGameStatus(), is(GameStatus.FINISHED));
    }
}