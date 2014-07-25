package se.freddejones.game.yakutia.model;

public class PlayerId {

    private final Long playerId;

    public PlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerId)) return false;

        PlayerId playerId1 = (PlayerId) o;

        if (playerId != null ? !playerId.equals(playerId1.playerId) : playerId1.playerId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return playerId != null ? playerId.hashCode() : 0;
    }
}
