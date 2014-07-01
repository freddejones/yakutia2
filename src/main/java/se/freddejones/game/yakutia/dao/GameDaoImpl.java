package se.freddejones.game.yakutia.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.InvitedPlayer;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class GameDaoImpl implements GameDao {

    private SessionFactory sessionFactory;

    @Autowired
    public GameDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public GameId createNewGame(CreateGameDTO createGameDTO) {
        Session session = sessionFactory.getCurrentSession();

        Game game = setupGame(createGameDTO);
        session.saveOrUpdate(game);
        session.refresh(game);

        List<GamePlayer> gamePlayers = setupGamePlayers(createGameDTO, game);
        for (GamePlayer gp : gamePlayers) {
            session.saveOrUpdate(gp);
            session.refresh(game);
            game.getPlayers().add(gp);
            session.merge(game);
        }

        return new GameId(game.getGameId());
    }

    private List<GamePlayer> setupGamePlayers(CreateGameDTO createGameDTO, Game game) {
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(setupGamePlayer(createGameDTO.getCreatedByPlayerId(), game, GamePlayerStatus.ACCEPTED));

        for (InvitedPlayer invitedPlayer : createGameDTO.getInvites()) {
            gamePlayers.add(setupGamePlayer(invitedPlayer.getId(), game, GamePlayerStatus.INVITED));
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

    private Game setupGame(CreateGameDTO createGameDTO) {
        Game g = new Game();
        g.setCreationTime(new Date());
        g.setGameCreatorPlayerId(createGameDTO.getCreatedByPlayerId());
        g.setName(createGameDTO.getGameName());
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
