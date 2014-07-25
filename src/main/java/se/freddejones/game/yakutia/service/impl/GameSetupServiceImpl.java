package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.UnitType;
import se.freddejones.game.yakutia.service.GameSetupService;
import se.freddejones.game.yakutia.service.GameTerritoryHandlerService;

import java.util.*;

@Service("GameSetupService")
@Transactional(readOnly = true)
public class GameSetupServiceImpl implements GameSetupService {

    private static final int DEFAULT_INIT_STRENGTH = 3;
    private final GamePlayerDao gamePlayerDao;
    private final GameTerritoryHandlerService gameTerritoryHandlerService;

    @Autowired
    public GameSetupServiceImpl(GamePlayerDao gamePlayerDao, GameTerritoryHandlerService gameTerritoryHandlerService) {
        this.gamePlayerDao = gamePlayerDao;
        this.gameTerritoryHandlerService = gameTerritoryHandlerService;
    }

    @Override
    @Transactional(readOnly = false)
    public void initializeNewGame(Set<GamePlayer> gamePlayers) {

        ArrayList<Territory> territories = (ArrayList<Territory>) gameTerritoryHandlerService.getShuffledTerritories();
        GameId gameId = gamePlayers.iterator().next().getTheGameId();

        while(!territories.isEmpty()) {
            for (GamePlayer gamePlayer : gamePlayers) {
                if (!territories.isEmpty()) {
                    Territory territory = territories.get(0);
                    territories.remove(0);
                    Unit assignedUnit = createUnit(territory);
                    gamePlayerDao.updateUnitsToGamePlayer(gamePlayer.getTheGamePlayerId(), assignedUnit);
                } else {
                    addUnassignedUnitStrength(gamePlayer.getTheGamePlayerId(), 1);
                }
            }
        }


        for (GamePlayer gamePlayer : gamePlayers) {
            addUnassignedUnitStrength(gamePlayer.getTheGamePlayerId(), DEFAULT_INIT_STRENGTH);
        }

        scramblePlayerIdTurn(gamePlayers);
    }

    private void scramblePlayerIdTurn(Set<GamePlayer> gamePlayers) {
        List<GamePlayer> gamePlayerList = new ArrayList<>();
        gamePlayerList.addAll(gamePlayers);
        Collections.shuffle(gamePlayerList);
        int size = gamePlayerList.size();
        for (int i = 0; i < size; i++) {
            GamePlayer gp = gamePlayerList.get(i);
            if (i==0) {
                gp.setActivePlayerTurn(true);
            }

            int idx = i+1;
            if (i+1 == size) {
                idx = 0;
            }
            gp.setNextGamePlayerIdTurn(gamePlayerList.get(idx).getGamePlayerId());
            gamePlayerDao.updateGamePlayer(gp);
        }
    }

    private Unit createUnit(Territory territory) {
        Unit assignedUnit = new Unit();
        assignedUnit.setTypeOfUnit(UnitType.SOLDIER);
        assignedUnit.setTerritory(territory);
        assignedUnit.setStrength(1);
        return assignedUnit;
    }

    private void addUnassignedUnitStrength(GamePlayerId gamePlayerId, int strength) {

        Unit u = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId).getUnitByTerritory(Territory.UNASSIGNED_TERRITORY);
        Unit unassignedTerritory = u != null ? u : createUnit(Territory.UNASSIGNED_TERRITORY);
        unassignedTerritory.addStrength(strength);
        gamePlayerDao.updateUnitsToGamePlayer(gamePlayerId, unassignedTerritory);
    }

}
