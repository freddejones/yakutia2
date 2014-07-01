package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;

import java.util.List;

public interface GameSetupService {

    public void initializeNewGame(List<GamePlayer> gamePlayers) throws CouldNotCreateGameException;

}
