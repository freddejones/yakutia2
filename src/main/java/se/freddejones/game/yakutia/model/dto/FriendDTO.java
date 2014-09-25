package se.freddejones.game.yakutia.model.dto;

public class FriendDTO {

    private long playerId;
    private String name;

    public FriendDTO(long playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

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
}
