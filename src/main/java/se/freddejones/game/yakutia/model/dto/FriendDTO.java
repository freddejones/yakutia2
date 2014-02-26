package se.freddejones.game.yakutia.model.dto;


import se.freddejones.game.yakutia.model.statuses.FriendStatus;

public class FriendDTO extends PlayerDTO {

    private Long playerId;
    private Long friendId;
    private FriendStatus friendStatus;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public FriendStatus getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }
}
