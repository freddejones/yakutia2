package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.dto.FriendDTO;

import java.util.List;

public interface FriendService {

    void inviteFriend(Long playerId, Long playerToFriendInvite);
    List<Player> getFriendInvites(Long playerId);
    List<Player> getFriends(Long playerId);
    FriendDTO acceptFriendInvite(Long playerId, Long friendId);
    Boolean declineFriendInvite(Long playerId, Long friendId);
    List<FriendDTO> getInvitedAndAcceptedFriends(Long playerId);
}
