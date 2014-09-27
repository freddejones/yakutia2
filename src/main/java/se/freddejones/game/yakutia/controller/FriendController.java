package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.FriendInviteDTO;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.service.FriendService;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping(value = "/friend")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @RequestMapping(value  = "/invite", method = RequestMethod.POST)
    @ResponseBody
    public void inviteFriend(@RequestBody final FriendInviteDTO friendInviteDTO) {
        friendService.inviteFriend(new PlayerId(friendInviteDTO.getPlayerId()), new PlayerId(friendInviteDTO.getFriendId()));
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    @ResponseBody
    public void acceptFriendInvite(@RequestBody final FriendInviteDTO friendInviteDTO) {    // TODO change nameof FriendInviteDTO
        friendService.acceptFriendInvite(new PlayerId(friendInviteDTO.getPlayerId()), new PlayerId(friendInviteDTO.getFriendId()));
    }

    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    @ResponseBody
    public Boolean declineFriendInvite(@RequestBody final FriendInviteDTO friendInviteDTO) {
//        return friendService.declineFriendInvite(friendDTO.getPlayerId(), friendDTO.getFriendId());
        return null;
    }

    @RequestMapping(value = "/accepted/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<FriendDTO> getAllAcceptedFriends(@PathVariable("playerId") Long playerid) {
        return friendService.getAllAcceptedFriendsForPlayer(new PlayerId(playerid));
    }

    @RequestMapping(value = "/invites/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<FriendDTO> getAllFriends(@PathVariable("playerId") Long playerid) {
        return friendService.getAllFriendInvitesForPlayer(new PlayerId(playerid));
    }

    @RequestMapping(value  = "/non/friends/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<FriendInviteDTO> getNonFriends(@PathVariable("playerId") Long playerid) {

//        // TODO Extract this to the service bean, pretty please
//        List<Player> players = playerService.getAllPlayers();
//        Player currentPlayer = playerService.getPlayerById(playerid);
//
//        List<FriendDTO> nonFriends = new ArrayList<>();
//        for (Player player : players) {
//            if (!(currentPlayer.getPlayerId() == player.getPlayerId()) &&
//                    !isFriendOrInvited(currentPlayer, player)) {
//                FriendDTO friendDTO = new FriendDTO();
//                friendDTO.setPlayerId(player.getPlayerId());
//                friendDTO.setPlayerName(player.getName());
//                friendDTO.setEmail(player.getEmail());
//                nonFriends.add(friendDTO);
//            }
//        }
//        return nonFriends;
        return null;
    }



    private boolean isFriendOrInvited(Player currentPlayer, Player p) {
        return friendCheckerIterator(currentPlayer.getFriends().iterator(), p.getPlayerId())
                || friendInviteCheckerIterator(currentPlayer.getFriendRequests().iterator(), p.getPlayerId());

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
