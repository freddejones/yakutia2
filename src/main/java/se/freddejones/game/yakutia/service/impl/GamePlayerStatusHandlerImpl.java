package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.service.GamePlayerStatusHandler;

@Service
@Transactional(readOnly = true)
public class GamePlayerStatusHandlerImpl implements GamePlayerStatusHandler {

    private final GamePlayerDao gamePlayerDao;

    @Autowired
    public GamePlayerStatusHandlerImpl(GamePlayerDao gamePlayerDao) {
        this.gamePlayerDao = gamePlayerDao;
    }

    @Override
    @Transactional(readOnly = false)
    public void acceptGameInvite(GamePlayerId gamePlayerId) {
        updateStatus(gamePlayerId, GamePlayerStatus.ACCEPTED);
    }

    @Override
    @Transactional(readOnly = false)
    public void declineGameInvite(GamePlayerId gamePlayerId) {
        updateStatus(gamePlayerId, GamePlayerStatus.DECLINED);
    }

    private void updateStatus(GamePlayerId gamePlayerId, GamePlayerStatus status) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        gamePlayer.setGamePlayerStatus(status);
        gamePlayerDao.updateGamePlayer(gamePlayer);
    }
}
