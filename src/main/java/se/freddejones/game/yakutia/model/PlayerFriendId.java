package se.freddejones.game.yakutia.model;

public class PlayerFriendId {

    private final Long playerFriendId;

    public PlayerFriendId(Long playerFriendId) {
        this.playerFriendId = playerFriendId;
    }

    public Long getPlayerFriendId() {
        return playerFriendId;
    }

    @Override
    public String toString() {
        return "PlayerFriendId{" +
                "playerFriendId=" + playerFriendId +
                '}';
    }
}
