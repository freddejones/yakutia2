package se.freddejones.game.yakutia.model.translators;

import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreateGameBinder {

    public CreateGame bind(CreateGameDTO createGameDTO) {
        PlayerId playerId = new PlayerId(createGameDTO.getCreatedByPlayerId());
        return  new CreateGame(playerId, createGameDTO.getGameName(), getInvitedPlayerIds(createGameDTO.getInvites()));
    }

    private Collection<PlayerId> getInvitedPlayerIds(List<Long> invites) {
        List<PlayerId> playerIds = new ArrayList<>();
        for (Long invite : invites) {
            playerIds.add(new PlayerId(invite));
        }
        return playerIds;
    }

}
