package se.freddejones.game.yakutia.dao.impl;

import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.PlayerFriend;

@Repository
public class PlayerFriendDaoImpl extends AbstractDaoImpl implements PlayerFriendDao {

    @Override
    public void persistPlayerFriendEntity(PlayerFriend playerFriend) {
        getCurrentSession().saveOrUpdate(playerFriend);
    }

    @Override
    public PlayerFriend getPlayerFriend(Long playerId, Long friendId) {
        return (PlayerFriend) getCurrentSession()
                .getNamedQuery("PlayerFriend.GetPlayerFriendByFriendIdAndPlayerId")
                .setParameter("playerId", playerId)
                .setParameter("friendId", friendId)
                .uniqueResult();
    }

    @Override
    public void mergePlayerFriendEntity(PlayerFriend playerFriend) {
        getCurrentSession().merge(playerFriend);
    }

    @Override
    public void deletePlayerFriend(PlayerFriend playerFriend) {
        getCurrentSession().delete(playerFriend);
    }


}
