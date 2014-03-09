package se.freddejones.game.yakutia.entity;

import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "GAME_PLAYERS_CON")
@NamedQueries({
        @NamedQuery(
                name = "GamePlayer.getGamePlayersFromPlayerId",
                query = "SELECT gp FROM GamePlayer gp WHERE gp.playerId =:playerId"
        ),
        @NamedQuery(
                name = "GamePlayer.getGamePlayersFromPlayerIdAndGameId",
                query = "SELECT gp FROM GamePlayer gp WHERE gp.playerId =:playerId AND gp.gameId =:gameId"
        ),
        @NamedQuery(
                name = "GamePlayer.getGamePlayersForGameId",
                query = "SELECT gp FROM GamePlayer gp WHERE gp.gameId =:gameId"
        )
})
public class GamePlayer implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "GAME_PLAYER_ID")
    private long gamePlayerId;

    @Column(name = "GAME_ID")
    private long gameId;

    @Column(name = "PLAYER_ID")
    private long playerId;

    @Enumerated(EnumType.STRING)
    private GamePlayerStatus gamePlayerStatus;

    @Enumerated(EnumType.STRING)
    private ActionStatus actionStatus;

    @Column(name = "NEXT_GAME_PLAYER_ID")
    private long nextGamePlayerIdTurn;

    @Column(name = "IS_CURRENT_PLAYER_TURN")
    private boolean activePlayerTurn;

    @ManyToOne
    @JoinColumn(name="GAME_ID", insertable = false, updatable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", insertable = false, updatable = false)
    private Player player;

    @OneToMany(mappedBy = "gamePlayer")
    private List<Unit> units;

    public long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public GamePlayerStatus getGamePlayerStatus() {
        return gamePlayerStatus;
    }

    public void setGamePlayerStatus(GamePlayerStatus gamePlayerStatus) {
        this.gamePlayerStatus = gamePlayerStatus;
    }

    public long getNextGamePlayerIdTurn() {
        return nextGamePlayerIdTurn;
    }

    public void setNextGamePlayerIdTurn(long nextGamePlayerIdTurn) {
        this.nextGamePlayerIdTurn = nextGamePlayerIdTurn;
    }

    public boolean isActivePlayerTurn() {
        return activePlayerTurn;
    }

    public void setActivePlayerTurn(boolean activePlayerTurn) {
        this.activePlayerTurn = activePlayerTurn;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Unit getUnitByTerritory(Territory territory) {
        for (Unit u : getUnits()) {
            if (u.getTerritory() == territory) {
                return u;
            }
        }
        return null;
    }
}
