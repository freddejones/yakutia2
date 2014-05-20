package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.service.FriendService;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @Autowired
    PlayerService playerService;

    @RequestMapping(value  = "/invite", method = RequestMethod.POST)
    @ResponseBody
    public void inviteFriend(@RequestBody final FriendDTO friendDTO) {
        friendService.inviteFriend(friendDTO.getPlayerId(), friendDTO.getFriendId());
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    @ResponseBody
    public FriendDTO acceptFriendInvite(@RequestBody final FriendDTO friendDTO) {
        return friendService.acceptFriendInvite(friendDTO.getPlayerId(), friendDTO.getFriendId());
    }

    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    @ResponseBody
    public Boolean declineFriendInvite(@RequestBody final FriendDTO friendDTO) {
        return friendService.declineFriendInvite(friendDTO.getPlayerId(), friendDTO.getFriendId());
    }

    @RequestMapping(value = "/get/all/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<FriendDTO> getAllFriends(@PathVariable("playerId") Long playerid) {
        return friendService.getInvitedAndAcceptedFriends(playerid);
    }

    @RequestMapping(value  = "/non/friends/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<FriendDTO> getNonFriends(@PathVariable("playerId") Long playerid) {

        // TODO Extract this to the service bean, pretty please
        List<Player> players = playerService.getAllPlayers();
        Player currentPlayer = playerService.getPlayerById(playerid);

        List<FriendDTO> nonFriends = new ArrayList<>();
        for (Player player : players) {
            if (!(currentPlayer.getPlayerId() == player.getPlayerId()) &&
                    !isFriendOrInvited(currentPlayer, player)) {
                FriendDTO friendDTO = new FriendDTO();
                friendDTO.setPlayerId(player.getPlayerId());
                friendDTO.setPlayerName(player.getName());
                friendDTO.setEmail(player.getEmail());
                nonFriends.add(friendDTO);
            }
        }
        return nonFriends;
    }



    private boolean isFriendOrInvited(Player currentPlayer, Player p) {
        return friendCheckerIterator(currentPlayer.getFriends().iterator(), p.getPlayerId())
                || friendInviteCheckerIterator(currentPlayer.getFriendsReqested().iterator(), p.getPlayerId());

    }

    private boolean friendCheckerIterator(Iterator<PlayerFriend> iterator, Long playerId) {
        while(iterator.hasNext()) {
            PlayerFriend playerFriend = iterator.next();
            if (playerFriend.getFriend().getPlayerId() == playerId) {
                return true;
            }
        }
        return false;
    }

    private boolean friendInviteCheckerIterator(Iterator<PlayerFriend> iterator, Long playerId) {
        while(iterator.hasNext()) {
            PlayerFriend playerFriend = iterator.next();
            if (playerFriend.getPlayer().getPlayerId() == playerId) {
                return true;
            }
        }
        return false;
    }
}
