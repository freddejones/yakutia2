package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.service.FriendService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

    @Override
    @Transactional(readOnly = false)
    public void inviteFriend(PlayerId playerId, PlayerId playerIdToInvite) {

        Player player = playerDao.getPlayerById(playerId);
        Player friend = playerDao.getPlayerById(playerIdToInvite);

        PlayerFriend playerFriend = new PlayerFriend();
        playerFriend.setFriendStatus(FriendStatus.INVITED);
        playerFriend.setPlayer(player);
        playerFriend.setFriend(friend);
        playerFriendDao.persistPlayerFriendEntity(playerFriend);

        player.setFriends(new HashSet<>(Arrays.asList(playerFriend)));
        playerDao.mergePlayer(player);
        friend.setFriendRequests(new HashSet<>(Arrays.asList(playerFriend)));
        playerDao.mergePlayer(friend);
    }

    @Override
    public List<FriendDTO> getAllFriendInvitesForPlayer(PlayerId playerId) {
        Player player = playerDao.getPlayerById(playerId);
        List<FriendDTO> filteredFriends = new ArrayList<>();

        player.getFriendRequests().stream().filter(playerFriend -> playerFriend.getFriendStatus() == FriendStatus.INVITED).forEach(playerFriend -> {
            Player playerWhoInvited = playerFriend.getPlayer();
            FriendDTO friendDTO = new FriendDTO(playerWhoInvited.getPlayerId(), playerWhoInvited.getName());
            filteredFriends.add(friendDTO);
        });

        return filteredFriends;
    }

    @Override
    public List<FriendDTO> getAllAcceptedFriendsForPlayer(PlayerId playerId) {
        Player player = playerDao.getPlayerById(playerId);
        List<FriendDTO> filteredFriends = new ArrayList<>();

        player.getFriends().stream().filter(playerFriend -> playerFriend.getFriendStatus() == FriendStatus.ACCEPTED).forEach(playerFriend -> {
            Player acceptedFriend = playerFriend.getFriend();
            FriendDTO friendDTO = new FriendDTO(acceptedFriend.getPlayerId(), acceptedFriend.getName());
            filteredFriends.add(friendDTO);
        });
        return filteredFriends;
    }

    @Override
    @Transactional(readOnly = false)
    public void acceptFriendInvite(PlayerId playerId, PlayerId friendId) {
        Player player = playerDao.getPlayerById(playerId);
        Player friendWhoInvited = playerDao.getPlayerById(friendId);

        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(friendId, playerId);
        playerFriend.setFriendStatus(FriendStatus.ACCEPTED);
        playerFriendDao.mergePlayerFriendEntity(playerFriend);

        PlayerFriend playerFriendForAcceptingPart = new PlayerFriend();
        playerFriendForAcceptingPart.setFriendStatus(FriendStatus.ACCEPTED);
        playerFriendForAcceptingPart.setPlayer(player);
        playerFriendForAcceptingPart.setFriend(friendWhoInvited);
        playerFriendDao.persistPlayerFriendEntity(playerFriendForAcceptingPart);
    }

    @Override
    @Transactional(readOnly = false)
    public void declineFriendInvite(PlayerId playerId, PlayerId friendId) {

    }



}
