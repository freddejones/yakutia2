package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.PlayerId;

import java.util.List;

public interface FriendService {

    void inviteFriend(PlayerId playerId, PlayerId playerIdToInvite);
    List<FriendDTO> getAllFriendInvitesForPlayer(PlayerId playerId);
    List<FriendDTO> getAllAcceptedFriendsForPlayer(PlayerId playerId);
    void acceptFriendInvite(PlayerId playerId, PlayerId friendId);
    void declineFriendInvite(PlayerId playerId, PlayerId friendId);
}
