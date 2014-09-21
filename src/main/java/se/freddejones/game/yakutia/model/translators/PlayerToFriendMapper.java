package se.freddejones.game.yakutia.model.translators;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;

import java.util.ArrayList;
import java.util.List;

public class PlayerToFriendMapper {

    public List<FriendDTO> map(List<Player> players) {
        List<FriendDTO> friendDTOs = new ArrayList<>();
        for (Player player : players) {
            friendDTOs.add(map(player));
        }
        return  friendDTOs;
    }

    private FriendDTO map(Player player) {
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setFriendId(player.getPlayerId());
        friendDTO.setPlayerName(player.getName());
        friendDTO.setFriendStatus(FriendStatus.ACCEPTED);
        return friendDTO;
    }

}
