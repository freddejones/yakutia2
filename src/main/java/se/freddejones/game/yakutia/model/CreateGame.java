package se.freddejones.game.yakutia.model;

import java.util.Collection;

public class CreateGame {

    private final PlayerId playerId;
    private final String gameName;
    private final Collection<PlayerId> invitedPlayers;

    public CreateGame(PlayerId playerId, String gameName, Collection<PlayerId> invitedPlayers) {
        this.playerId = playerId;
        this.gameName = gameName;
        this.invitedPlayers = invitedPlayers;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public String getGameName() {
        return gameName;
    }

    public Collection<PlayerId> getInvitedPlayers() {
        return invitedPlayers;
    }

    @Override
    public String toString() {
        return "CreateGame{" +
                "playerId=" + playerId +
                ", gameName='" + gameName + '\'' +
                ", invitedPlayers=" + invitedPlayers +
                '}';
    }
}
