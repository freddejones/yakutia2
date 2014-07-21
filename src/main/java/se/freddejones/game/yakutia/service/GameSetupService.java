package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;

import java.util.List;
import java.util.Set;

public interface GameSetupService {

    public void initializeNewGame(Set<GamePlayer> gamePlayers);

}
