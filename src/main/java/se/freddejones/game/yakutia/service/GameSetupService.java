package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;

import java.util.List;

/**
 * Created by fidde on 2014-02-13.
 */
public interface GameSetupService {

    public void initializeNewGame(List<GamePlayer> gamePlayers) throws CouldNotCreateGameException;

}
