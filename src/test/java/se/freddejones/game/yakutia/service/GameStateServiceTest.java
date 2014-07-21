package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.service.impl.GameStateServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class GameStateServiceTest {

    private GameStateService gameStateService;
    private GamePlayerDao gamePlayerDao;
    private UnitDao unitDao;
    private GamePlayer gamePlayer;

    @Before
    public void setUp() throws Exception {
        gamePlayerDao = mock(GamePlayerDao.class);
        GameService gameService = mock(GameService.class);
        unitDao = mock(UnitDao.class);
        gamePlayer = new GamePlayer();
        gameStateService = new GameStateServiceImpl(gamePlayerDao, gameService, unitDao);
    }

    @Test
    public void testGetTerritoryInformationForGamePlayer() {
        // given
        setupDefaultMockGamePlayerAndGameConnection();
        List<Unit> units = Arrays.asList(createUnit(UnitType.SOLDIER));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(any(GamePlayerId.class), any(Territory.class))).thenReturn(units);
        gamePlayer.setUnits(units);

        // when
        List<TerritoryInformation> territoryInformations = gameStateService.getTerritoryInformationForActiveGame(gamePlayer.getTheGamePlayerId());

        // then
        TerritoryInformation territoryInformation = territoryInformations.get(0);
        assertThat(territoryInformation.getTerritory(), is(Territory.ICELAND));
        assertThat(territoryInformation.getGamePlayerId(), is(gamePlayer.getTheGamePlayerId()));
        assertThat(territoryInformation.getUnits().size(), is(1));
    }

    @Test
    public void testGetCombinedUnitsInTerritoryInformationForGamePlayer() {
        // given
        setupDefaultMockGamePlayerAndGameConnection();
        List<Unit> units = Arrays.asList(createUnit(UnitType.SOLDIER), createUnit(UnitType.TANK));
        when(unitDao.getUnitsForGamePlayerIdAndTerritory(any(GamePlayerId.class), any(Territory.class))).thenReturn(units);
        gamePlayer.setUnits(units);

        // when
        List<TerritoryInformation> territoryInformations = gameStateService.getTerritoryInformationForActiveGame(gamePlayer.getTheGamePlayerId());

        // then
        TerritoryInformation territoryInformation = territoryInformations.get(0);
        assertThat(territoryInformation.getUnits().size(), is(2));
    }

    private void setupDefaultMockGamePlayerAndGameConnection() {
        GameId gameId = new GameId(1L);
        gamePlayer.setGamePlayerId(1L);
        gamePlayer.setGameId(1L);
        when(gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayer.getTheGamePlayerId())).thenReturn(gamePlayer);
        when(gamePlayerDao.getGamePlayersByGameId(gameId)).thenReturn(Arrays.asList(gamePlayer));
    }

    private Unit createUnit(UnitType unitType) {
        Unit unit = new Unit();
        unit.setStrength(1);
        unit.setTerritory(Territory.ICELAND);
        unit.setTypeOfUnit(unitType);
        return unit;
    }
}