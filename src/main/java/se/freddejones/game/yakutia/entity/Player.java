package se.freddejones.game.yakutia.entity;

import se.freddejones.game.yakutia.model.dto.PlayerDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PLAYER")
@NamedQueries({
        @NamedQuery(
                name = "Player.getAllPlayers",
                query = "SELECT p FROM Player p"
        ),
        @NamedQuery(
                name = "Player.getPlayerByEmail",
                query = "SELECT p FROM Player p WHERE email LIKE :email"
        )
})
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PLAYER_ID")
    private long playerId;
    @Column(name = "PLAYER_NAME")
    private String name;
    @Column(name = "EMAIL", unique = true)
    private String email;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> games;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<PlayerFriend> friends = new HashSet<>();

    @OneToMany(mappedBy = "friend", fetch = FetchType.EAGER)
    private Set<PlayerFriend> friendsReqested = new HashSet<>();

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GamePlayer> getGames() {
        return games;
    }

    public void setGames(Set<GamePlayer> games) {
        this.games = games;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PlayerFriend> getFriends() {
        return friends;
    }

    public void setFriends(Set<PlayerFriend> friends) {
        this.friends = friends;
    }

    public Set<PlayerFriend> getFriendsReqested() {
        return friendsReqested;
    }

    public void setFriendsReqested(Set<PlayerFriend> friendsReqested) {
        this.friendsReqested = friendsReqested;
    }
}
