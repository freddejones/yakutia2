package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("gameDao")
public class GameDaoImpl implements GameDao {

    private SessionFactory sessionFactory;

    @Autowired
    public GameDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public GameId createNewGame(CreateGame createGame) {
        Session session = sessionFactory.getCurrentSession();

        Game game = setupGame(createGame);
        session.saveOrUpdate(game);
        session.refresh(game);

        List<GamePlayer> gamePlayers = setupGamePlayers(createGame, game);
        for (GamePlayer gp : gamePlayers) {
            session.saveOrUpdate(gp);
            session.refresh(game);
            game.getPlayers().add(gp);
            session.merge(game);
        }

        return new GameId(game.getGameId());
    }

    private List<GamePlayer> setupGamePlayers(CreateGame createGame, Game game) {
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(setupGamePlayer(createGame.getPlayerId().getPlayerId(), game, GamePlayerStatus.ACCEPTED));

        for (PlayerId invitedPlayerId : createGame.getInvitedPlayers()) {
            gamePlayers.add(setupGamePlayer(invitedPlayerId.getPlayerId(), game, GamePlayerStatus.INVITED));
        }
        return gamePlayers;
    }

    private GamePlayer setupGamePlayer(Long playerId, Game game, GamePlayerStatus status) {
        GamePlayer gp = new GamePlayer();
        gp.setGame(game);
        gp.setGameId(game.getGameId());
        Player p = (Player) sessionFactory.getCurrentSession().get(Player.class, playerId);
        gp.setPlayer(p);
        gp.setPlayerId(p.getPlayerId());
        gp.setGamePlayerStatus(status);
        gp.setActivePlayerTurn(false);
        return gp;
    }

    private Game setupGame(CreateGame createGame) {
        Game g = new Game();
        g.setCreationTime(new LocalDate().toDate());
        g.setGameCreatorPlayerId(createGame.getPlayerId().getPlayerId());
        g.setName(createGame.getGameName());
        g.setGameStatus(GameStatus.CREATED);
        return g;
    }

    @Override
    public Game getGameByGameId(GameId gameId) {
        return (Game)sessionFactory.getCurrentSession().get(Game.class, gameId.getGameId());
    }

    @Override
    public void startGame(GameId gameId) {
        Session session = sessionFactory.getCurrentSession();
        Game realGame = (Game)session.get(Game.class, gameId.getGameId());
        realGame.setGameStatus(GameStatus.ONGOING);
        realGame.setStartedTime(new Date());
        session.saveOrUpdate(realGame);
    }

    @Override
    public void endGame(GameId gameId) {
        Session session = sessionFactory.getCurrentSession();
        Game game = (Game) session.get(Game.class, gameId.getGameId());
        game.setGameStatus(GameStatus.FINISHED);
        game.setFinshedTime(new Date());
        session.saveOrUpdate(game);
    }
}
