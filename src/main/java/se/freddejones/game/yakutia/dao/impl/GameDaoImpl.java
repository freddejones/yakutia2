package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.InvitedPlayer;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class GameDaoImpl extends AbstractDaoImpl implements GameDao {

    @Override
    public Long createNewGame(CreateGameDTO createGameDTO) {
        Session session = getCurrentSession();

        Game game = setupGame(createGameDTO);
        session.saveOrUpdate(game);
        session.refresh(game);

        List<GamePlayer> gamePlayers = setupGamePlayers(createGameDTO, game);
        for (GamePlayer gp : gamePlayers) {
            getCurrentSession().saveOrUpdate(gp);
            session.refresh(game);
            game.getPlayers().add(gp);
            session.merge(game);
        }

        return game.getGameId();
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
        Player p = (Player) getCurrentSession().get(Player.class, playerId);
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
    public Game getGameByGameId(long gameId) {
        return (Game)getCurrentSession().get(Game.class, gameId);
    }

    @Override
    public void startGame(long gameId) {
        Session session = getCurrentSession();
        Game realGame = (Game)session.get(Game.class, gameId);
        realGame.setGameStatus(GameStatus.ONGOING);
        realGame.setStartedTime(new Date());
        session.saveOrUpdate(realGame);
    }

    @Override
    public void endGame(long gameId) {
        Session session = getCurrentSession();
        Game game = (Game) session.get(Game.class, gameId);
        game.setGameStatus(GameStatus.FINISHED);
        game.setFinshedTime(new Date());
        session.saveOrUpdate(game);
    }
}
