package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.service.GameService;
import se.freddejones.game.yakutia.service.GameStateService;

import java.util.*;

@Service
public class GameStateServiceImpl implements GameStateService {

    private final GamePlayerDao gamePlayerDao;
    private final GameService gameService;
    private final UnitDao unitDao;

    @Autowired
    public GameStateServiceImpl(GamePlayerDao gamePlayerDao, GameService gameService, UnitDao unitDao) {
        this.gamePlayerDao = gamePlayerDao;
        this.gameService = gameService;
        this.unitDao = unitDao;
    }

    @Override
    public List<TerritoryInformation> getTerritoryInformationForActiveGame(GamePlayerId gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerId);
        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gamePlayer.getTheGameId());
        List<TerritoryInformation> territoryInformation = translate(gamePlayers);
        return territoryInformation;
    }

    private List<TerritoryInformation> translate(List<GamePlayer> gamePlayers) {
        List<TerritoryInformation> territoryInformationList = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            Set<Territory> territorySet = gamePlayer.getAllTerritoriesForGamePlayer();
            for (Territory territory : territorySet) {
                List<Unit> units = unitDao.getUnitsForGamePlayerIdAndTerritory(gamePlayer.getTheGamePlayerId(),territory);
                TerritoryInformation territoryInformation = new TerritoryInformation(territory,combineUnits(units),gamePlayer.getTheGamePlayerId());
                territoryInformationList.add(territoryInformation);
            }
        }
        return territoryInformationList;
    }

    private Map<UnitType, Integer> combineUnits(List<Unit> units) {
        Map<UnitType, Integer> unitComponent = new HashMap<>();
        for(Unit unit : units) {
            unitComponent.put(unit.getTypeOfUnit(), unit.getStrength());
        }
        return unitComponent;
    }

}
