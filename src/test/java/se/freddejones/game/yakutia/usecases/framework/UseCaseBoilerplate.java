package se.freddejones.game.yakutia.usecases.framework;

import org.codehaus.jackson.map.ObjectMapper;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;

import java.io.IOException;

public class UseCaseBoilerplate {

    public static FriendDTO createFriendDTO() {
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setPlayerId(1L);
        friendDTO.setFriendId(2L);
        return friendDTO;
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