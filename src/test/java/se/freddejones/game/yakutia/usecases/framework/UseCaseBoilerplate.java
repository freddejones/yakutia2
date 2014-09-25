package se.freddejones.game.yakutia.usecases.framework;

import org.codehaus.jackson.map.ObjectMapper;
import se.freddejones.game.yakutia.model.FriendInviteDTO;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;

import java.io.IOException;

public class UseCaseBoilerplate {

    public static FriendInviteDTO createFriendDTO() {
        FriendInviteDTO friendInviteDTO = new FriendInviteDTO();
        friendInviteDTO.setPlayerId(1L);
        friendInviteDTO.setFriendId(2L);
        return friendInviteDTO;
    }

    public static PlayerDTO createPlayerDTO() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerName("tomten");
        playerDTO.setEmail("fräs@frässish");
        return playerDTO;
    }

    public static byte[] convertDtoToByteArray(Object obj) throws IOException {
        return new ObjectMapper().writeValueAsBytes(obj);
    }
}
