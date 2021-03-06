package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.PlayerFriendId;
import se.freddejones.game.yakutia.model.PlayerId;

import java.util.List;

@Repository("playerFriendDao")
public class PlayerFriendDaoImpl implements PlayerFriendDao {

    private final SessionFactory sessionFactory;


    @Autowired
    public PlayerFriendDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PlayerFriendId persistPlayerFriendEntity(PlayerFriend playerFriend) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(playerFriend);
        currentSession.flush();
        return new PlayerFriendId(playerFriend.getPlayerFriendId());
    }

    // TODO remove this
    public List<PlayerFriend> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT pf from PlayerFriend pf").list();
    }

    @Override
    public PlayerFriend getPlayerFriend(PlayerId playerId, PlayerId friendId) {
        return (PlayerFriend) sessionFactory.getCurrentSession()
                .getNamedQuery("PlayerFriend.GetPlayerFriendByFriendIdAndPlayerId")
                .setParameter("playerId", playerId.getPlayerId())
                .setParameter("friendId", friendId.getPlayerId())
                .uniqueResult();
    }

    @Override
    public PlayerFriend getPlayerFriendById(PlayerFriendId playerFriendId) {
        return (PlayerFriend) sessionFactory.getCurrentSession().get(PlayerFriend.class, playerFriendId.getPlayerFriendId());
    }


    @Override
    public void mergePlayerFriendEntity(PlayerFriend playerFriend) {
        sessionFactory.getCurrentSession().saveOrUpdate(playerFriend);
    }

    @Override
    public void deletePlayerFriend(PlayerFriend playerFriend) {
        sessionFactory.getCurrentSession().delete(playerFriend);
    }


}
