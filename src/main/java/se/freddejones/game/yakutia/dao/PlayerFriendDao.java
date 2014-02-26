package se.freddejones.game.yakutia.dao;


import se.freddejones.game.yakutia.entity.PlayerFriend;

public interface PlayerFriendDao {

    void persistPlayerFriendEntity(PlayerFriend playerFriend);
    PlayerFriend getPlayerFriend(Long playerId, Long friendId);
    void mergePlayerFriendEntity(PlayerFriend playerFriend);
    void deletePlayerFriend(PlayerFriend playerFriend);

}
