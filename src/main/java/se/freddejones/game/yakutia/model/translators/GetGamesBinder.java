package se.freddejones.game.yakutia.model.translators;


import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.GameDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GetGamesBinder {


    public List<GameDTO> bind(List<Game> games, PlayerId playerId) {
        List<GameDTO> gameDTOs = new ArrayList<>();
        for (Game game : games) {
            gameDTOs.add(bind(game, playerId));
        }
        return gameDTOs;
    }

    protected GameDTO bind(Game game, PlayerId playerId) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getGameId());
        gameDTO.setDate(game.getCreationTime().toString());
        gameDTO.setName(game.getName());
        gameDTO.setStatus(game.getGameStatus().toString());
        gameDTO.setCanStartGame(game.isCreatorOfGame(playerId.getPlayerId()));
        gameDTO.setPlayersTurn(checkIsPlayersTurn(game.getPlayers(), playerId));
        return gameDTO;
    }

    private boolean checkIsPlayersTurn(Set<GamePlayer> gamePlayers, PlayerId playerId) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getPlayerId() == playerId.getPlayerId()) {
                return gamePlayer.isActivePlayerTurn();
            }
        }
        return false;
    }
}
