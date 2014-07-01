package se.freddejones.game.yakutia.dao;

import org.h2.tools.Server;
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
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.InvitedPlayer;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class GameDaoTest {

    @Configuration
    @Import(HibernateConfig.class)
    @ComponentScan(basePackages = {"se.freddejones.game.yakutia.dao"})
    static class TestConfiguration {}

    @BeforeClass
    public static void setup() throws Exception {
        TestdataHandler.resetAndRebuild();
        TestdataHandler.loadChangeSet("src/test/resources/db/testdata/game_gameplayers_players.xml");
    }

    @Autowired
    private GameDao gameDao;

    @Test
    public void testShouldCreateGame() throws SQLException, ClassNotFoundException {
        // given
        // test data loaded
        CreateGameDTO createGameDto = new CreateGameDTO();
        createGameDto.setGameName("new game");
        createGameDto.setCreatedByPlayerId(1L);
        List<InvitedPlayer> invitedPlayers = new ArrayList<>();
        InvitedPlayer invitedPlayer = new InvitedPlayer();
        invitedPlayer.setId(2L);
        invitedPlayers.add(invitedPlayer);
        createGameDto.setInvites(invitedPlayers);

        // when
        GameId gameId = gameDao.createNewGame(createGameDto);

        // then
        assertThat(gameId.getGameId(), is(notNullValue()));
    }

    @Test
    public void testShouldFetchGameById() {
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