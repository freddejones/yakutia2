package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.model.GamePlayerId;

public interface GamePlayerStatusHandler {

    public void acceptGameInvite(GamePlayerId gamePlayerId);
    public void declineGameInvite(GamePlayerId gamePlayerId);

}
