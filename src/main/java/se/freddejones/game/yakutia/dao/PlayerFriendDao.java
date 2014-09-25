package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.PlayerFriendId;
import se.freddejones.game.yakutia.model.PlayerId;

import java.util.List;

public interface PlayerFriendDao {

    PlayerFriendId persistPlayerFriendEntity(PlayerFriend playerFriend);
    PlayerFriend getPlayerFriend(PlayerId playerId, PlayerId friendId);
    PlayerFriend getPlayerFriendById(PlayerFriendId playerFriendId);
    void mergePlayerFriendEntity(PlayerFriend playerFriend);
    void deletePlayerFriend(PlayerFriend playerFriend);
    List<PlayerFriend> getAll();
}
