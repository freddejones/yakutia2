package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.service.FriendService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("friendservice")
@Transactional(readOnly = true)
public class DefaultFriendService implements FriendService {

    private PlayerFriendDao playerFriendDao;
    private PlayerDao playerDao;

    @Autowired
    public DefaultFriendService(PlayerDao playerDao, PlayerFriendDao playerFriendDao) {
        this.playerDao = playerDao;
        this.playerFriendDao = playerFriendDao;
    }



//    @Override
//    @Transactional(readOnly = false)
//    public void inviteFriend(Long playerId, Long playerToFriendInvite) {
//        Player player = playerDao.getPlayerById(playerId);
//        Player friendToInvite = playerDao.getPlayerById(playerToFriendInvite);
//        PlayerFriend pf = new PlayerFriend();
//        pf.setFriendStatus(FriendStatus.INVITED);
//        pf.setPlayer(player);
//        pf.setFriend(friendToInvite);
//        playerFriendDao.persistPlayerFriendEntity(pf);
//    }
//
//    @Override
//    public List<Player> getAllFriendInvites(Long playerId) {
//        List<Player> invitedFriends = new ArrayList<>();
//        Player p = playerDao.getPlayerById(playerId);
//        final Set<PlayerFriend> friends = p.getFriendsReqested();
//        Iterator<PlayerFriend> playerFriendIterator = friends.iterator();
//        while(playerFriendIterator.hasNext()) {
//            PlayerFriend playerFriend = playerFriendIterator.next();
//            if (playerFriend.getFriendStatus() == FriendStatus.INVITED) {
//                invitedFriends.add(playerFriend.getPlayer());
//            }
//        }
//        return invitedFriends;
//    }
//
//    @Override
//    public List<Player> getFriends(Long playerId) {
//        List<Player> friendsAccepted = new ArrayList<Player>();
//        Player p = playerDao.getPlayerById(playerId);
//        final Set<PlayerFriend> friends = p.getFriends();
//        Iterator<PlayerFriend> playerFriendIterator = friends.iterator();
//        while(playerFriendIterator.hasNext()) {
//            PlayerFriend playerFriend = playerFriendIterator.next();
//            if (playerFriend.getFriendStatus() == FriendStatus.ACCEPTED) {
//                friendsAccepted.add(playerFriend.getFriend());
//            }
//        }
//        return friendsAccepted;
//    }
//
//    @Override
//    @Transactional(readOnly = false)
//    public FriendDTO acceptFriendInvite(Long invitedPlayer, Long playerWhoInvited) {
//        // TODO clean this method up (extract it)
//        FriendDTO friendDTO = new FriendDTO();
//        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(playerWhoInvited, invitedPlayer);
//        playerFriend.setFriendStatus(FriendStatus.ACCEPTED);
//        playerFriendDao.mergePlayerFriendEntity(playerFriend);
//        friendDTO.setFriendStatus(FriendStatus.ACCEPTED);
//        friendDTO.setPlayerName(playerFriend.getPlayer().getName());
//        friendDTO.setFriendId(playerWhoInvited);
//
//        PlayerFriend playerFriendReplicateLeg = new PlayerFriend();
//        playerFriendReplicateLeg.setFriend(playerFriend.getPlayer());
//        playerFriendReplicateLeg.setPlayer(playerFriend.getFriend());
//        playerFriendReplicateLeg.setFriendStatus(FriendStatus.ACCEPTED);
//        playerFriendDao.persistPlayerFriendEntity(playerFriendReplicateLeg);
//        return friendDTO;
//    }
//
//    @Override
//    @Transactional(readOnly = false)
//    public Boolean declineFriendInvite(Long playerId, Long friendId) {
//        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(friendId, playerId);
//        playerFriendDao.deletePlayerFriend(playerFriend);
//        return playerFriendDao.getPlayerFriend(friendId, playerId) == null;
//    }
//
//    @Override
//    public List<FriendDTO> getInvitedAndAcceptedFriends(Long playerId) {
//        List<FriendDTO> friendDTOs = new ArrayList<>();
//        List<Player> invites = getAllFriendInvites(playerId);
//        List<Player> friends = getFriends(playerId);
//
//        // TODO refactor this:
//        mapFriendDto(friendDTOs, playerId, invites, FriendStatus.INVITED);
//        mapFriendDto(friendDTOs, playerId, friends, FriendStatus.ACCEPTED);
//        return friendDTOs;
//    }



    @Override
    public void inviteFriend(PlayerId playerId, PlayerId playerToFriendInvite) {

    }

    @Override
    public List<Player> getAllFriendInvites(PlayerId playerId) {
        Player player = playerDao.getPlayerById(playerId);
        return getFriendsBasedOnStatus(player.getFriends(), FriendStatus.INVITED);
    }

    @Override
    public List<Player> getAllAcceptedFriends(PlayerId playerId) {
        Player player = playerDao.getPlayerById(playerId);
        return getFriendsBasedOnStatus(player.getFriends(), FriendStatus.ACCEPTED);
    }

    private List<Player> getFriendsBasedOnStatus(Set<PlayerFriend> friends, FriendStatus status) {
        List<Player> filteredFriends = new ArrayList<>();
        for (PlayerFriend playerFriend : friends) {
            if (playerFriend.getFriendStatus() == status) {
                filteredFriends.add(playerFriend.getFriend());
            }
        }
        return filteredFriends;
    }

    @Override
    public FriendDTO acceptFriendInvite(PlayerId playerId, Long friendId) {
        return null;
    }

    @Override
    public Boolean declineFriendInvite(PlayerId playerId, Long friendId) {
        return null;
    }

}
