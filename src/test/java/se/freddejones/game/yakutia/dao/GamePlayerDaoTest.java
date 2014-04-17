package se.freddejones.game.yakutia.dao;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.UnitType;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:**/hibernateTestContext.xml"
        ,"classpath*:**/applicationTestContext.xml"
})
@Transactional(readOnly = false)
public class GamePlayerDaoTest {

    public static final String GAME_NAME = "GAME_NAME";
    public static final String TEST_EMAIL = "test@email.com";
    public static final String PLAYER_NAME = "PLAYER_NAME";
    @Autowired
    private GamePlayerDao gamePlayerDao;

    private Long playerId;
    private Long gameId;

    @Before
    public void setup() {
        Session session = gamePlayerDao.getSession();
        Player p = new Player();
        p.setEmail(TEST_EMAIL);
        p.setName(PLAYER_NAME);
        session.persist(p);
        session.refresh(p);
        playerId = p.getPlayerId();

        Game g = new Game();
        g.setCreationTime(new Date());
        g.setGameCreatorPlayerId(p.getPlayerId());
        g.setName(GAME_NAME);
        g.setGameStatus(GameStatus.CREATED);
        session.persist(g);
        gameId = g.getGameId();
        session.refresh(g);

        GamePlayer gp = new GamePlayer();
        gp.setGame(g);
        gp.setGameId(g.getGameId());
        gp.setPlayer(p);
        gp.setPlayerId(p.getPlayerId());
        gp.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        gp.setActivePlayerTurn(false);
        session.persist(gp);

        Unit unit = new Unit();
        unit.setStrength(1);
        unit.setTerritory(Territory.SWEDEN);
        unit.setTypeOfUnit(UnitType.TANK);
        unit.setGamePlayer(gp);
        List<Unit> units = new ArrayList<>();
        units.add(unit);
        gp.setUnits(units);
        session.persist(gp);
    }

    @Test
    public void testUpdateStrengthSettingsOnUnit() throws Exception {
        // given (gameplayer with one unit)
        GamePlayer gp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
        assertThat(gp.getUnits().size(), is(1));
        assertThat(gp.getUnits().get(0).getStrength(), is(1));

        Unit u = gp.getUnits().get(0);
        u.setStrength(0);

        // when
        gamePlayerDao.setUnitsToGamePlayer(gp.getGamePlayerId(), u);

        // then (refresh gameplayer object from db)
        gp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
        assertThat(gp.getUnits().size(), is(1));
        assertThat(gp.getUnits().get(0).getStrength(), is(0));
    }
}
