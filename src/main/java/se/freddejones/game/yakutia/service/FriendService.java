package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.FriendDTO;

import java.util.List;

public interface FriendService {

    void inviteFriend(PlayerId playerId, PlayerId playerToFriendInvite);
    List<Player> getFriendInvites(PlayerId playerId);
    List<Player> getFriends(PlayerId playerId);
    FriendDTO acceptFriendInvite(PlayerId playerId, Long friendId);
    Boolean declineFriendInvite(PlayerId playerId, Long friendId);
    List<FriendDTO> getInvitedAndAcceptedFriends(PlayerId playerId);
}
