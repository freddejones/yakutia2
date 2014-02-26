package se.freddejones.game.yakutia.model.statuses;

/**
 * User: Fredde
 * Date: 7/7/13 8:50 PM
 */
public enum FriendStatus {

    INVITED ("INVITED"),
    ACCEPTED ("ACCEPTED"),
    DECLINED ("DECLINED");

    private String friendStatus;

    private FriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    @Override
    public String toString() {
        return friendStatus;
    }

}
