package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.CouldNotCreateGameException;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.GameSetup;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.UnitType;
import se.freddejones.game.yakutia.service.GameSetupService;

import java.util.ArrayList;
import java.util.List;

import static se.freddejones.game.yakutia.model.GameManager.getLandAreas;

@Service("GameSetupService")
@Transactional(readOnly = true)
public class GameSetupServiceImpl implements GameSetupService {

    @Autowired
    protected GamePlayerDao gamePlayerDao;

    @Override
    @Transactional(readOnly = false)
    public void initializeNewGame(List<GamePlayer> gamePlayers) throws CouldNotCreateGameException {
        List<GameSetup> gamePlayerSetups = new ArrayList<>();

        for (GamePlayer gamePlayer : gamePlayers) {
            GameSetup gameSetup = new GameSetup();
            gameSetup.setGp(gamePlayer);
            gameSetup.setUnits(new ArrayList<Unit>());
            gameSetup.setTotalNumberOfUnits(10);
            gamePlayerSetups.add(gameSetup);
        }

        addTerritoryAndUnitsToGamePlayer(gamePlayers, gamePlayerSetups);

        for (GameSetup gss : gamePlayerSetups) {

            while(gss.getTotalNumberOfUnits() != 0) {
                for (Unit unit : gss.getUnits()) {
                    if (gss.getTotalNumberOfUnits() < 5) {
                        int currentStrength = unit.getStrength();
                        int newStrength = currentStrength + gss.getTotalNumberOfUnits();
                        unit.setStrength(newStrength);
                        gss.setTotalNumberOfUnits(gss.getTotalNumberOfUnits()-newStrength);
                    } else {
                        unit.setStrength(unit.getStrength()+5);
                        gss.setTotalNumberOfUnits(gss.getTotalNumberOfUnits()-5);
                    }
                }
            }

            GamePlayerId gamePlayerId = new GamePlayerId(gss.getGp().getGamePlayerId());
            for (Unit u : gss.getUnits()) {
                gamePlayerDao.setUnitsToGamePlayer(gamePlayerId, u);
            }

            Unit reinforcementUnit = new Unit();
            reinforcementUnit.setStrength(3);
            reinforcementUnit.setTypeOfUnit(UnitType.TANK);
            reinforcementUnit.setTerritory(Territory.UNASSIGNEDLAND);
            gamePlayerDao.setUnitsToGamePlayer(gamePlayerId, reinforcementUnit);
        }
    }

    private void addTerritoryAndUnitsToGamePlayer(List<GamePlayer> gamePlayers, List<GameSetup> gamePlayersSetup) {
        List<Territory> territories = getLandAreas();
        int gamePlayerCounter = 0;
        for(Territory territory : territories) {
            Unit u = new Unit();
            u.setTerritory(territory);
            u.setStrength(0);
            u.setTypeOfUnit(UnitType.TANK);
            gamePlayersSetup.get(gamePlayerCounter).getUnits().add(u);
            gamePlayerCounter++;
            if (gamePlayerCounter == gamePlayers.size()) {
                gamePlayerCounter = 0;
            }
        }
    }

}
