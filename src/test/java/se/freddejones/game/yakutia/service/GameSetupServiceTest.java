package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.service.impl.GameSetupServiceImpl;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class GameSetupServiceTest {

    private GamePlayerDao gamePlayerDao;
    private GameSetupService gameSetupService;
    private GameTerritoryHandlerService gameTerritoryHandlerService;
    private GamePlayerId gamePlayerOneId;
    private GamePlayerId gamePlayerTwoId;
    private GamePlayer gamePlayerOne;
    private GamePlayer gamePlayerTwo;
    private ArgumentCaptor<Unit> unitArgumentCaptor;
    private ArgumentCaptor<GamePlayerId> gamePlayerIdArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        gamePlayerDao = mock(GamePlayerDao.class);
        gameTerritoryHandlerService = mock(GameTerritoryHandlerService.class);
        gameSetupService = new GameSetupServiceImpl(gamePlayerDao, gameTerritoryHandlerService);
        gamePlayerOneId = new GamePlayerId(1L);
        gamePlayerTwoId = new GamePlayerId(2L);
        gamePlayerOne = new GamePlayer();
        gamePlayerOne.setGamePlayerId(gamePlayerOneId.getGamePlayerId());
        gamePlayerTwo = new GamePlayer();
        gamePlayerTwo.setGamePlayerId(gamePlayerTwoId.getGamePlayerId());
        unitArgumentCaptor = ArgumentCaptor.forClass(Unit.class);
        gamePlayerIdArgumentCaptor = ArgumentCaptor.forClass(GamePlayerId.class);
    }

    @Test
    public void testAllPlayersAreAssigned3UnitsOfUnassignedTerritory() {
        // given
        Set<GamePlayer> gamePlayerSet = defaultGamePlayerSet();
        setupMockForUnassignedTerritory();
        when(gameTerritoryHandlerService.getShuffledTerritories()).thenReturn(new ArrayList<Territory>());

        // when
        gameSetupService.initializeNewGame(gamePlayerSet);

        // then
        verify(gamePlayerDao, times(2)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
        assertThat(gamePlayerIdArgumentCaptor.getAllValues().containsAll(Arrays.asList(gamePlayerOneId, gamePlayerTwoId)), is(true));
        assertThat(unitArgumentCaptor.getAllValues().get(0).getTerritory(), is(Territory.UNASSIGNED_TERRITORY));
        assertThat(unitArgumentCaptor.getAllValues().get(1).getTerritory(), is(Territory.UNASSIGNED_TERRITORY));
        assertThat(unitArgumentCaptor.getAllValues().get(0).getStrength(), is(3));
        assertThat(unitArgumentCaptor.getAllValues().get(1).getStrength(), is(3));
    }

    @Test
    public void testAllPlayersAreAssignedAllEvenTerritories() {

        // given
        defaultTerritories();
        Set<GamePlayer> gamePlayerSet = defaultGamePlayerSet();
        setupMockForUnassignedTerritory();

        // when
        gameSetupService.initializeNewGame(gamePlayerSet);

        // then
        verify(gamePlayerDao, times(4)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
        List<Unit> units = unitArgumentCaptor.getAllValues();

        // first player
        assertThat(units.get(0).getTerritory(), is(Territory.DENMARK));
        assertThat(units.get(0).getStrength(), is(1));

        // second player
        assertThat(units.get(1).getTerritory(), is(Territory.FINLAND));
        assertThat(units.get(1).getStrength(), is(1));
    }

    @Test
    public void testAllPlayersAreaAssignedAllOddTerritories() {
        // given
        List<Territory> oddTerritories = defaultTerritories();
        oddTerritories.add(Territory.ICELAND);
        Set<GamePlayer> gamePlayerSet = defaultGamePlayerSet();
        when(gameTerritoryHandlerService.getShuffledTerritories()).thenReturn(oddTerritories);
        setupMockForUnassignedTerritory();

        // when
        gameSetupService.initializeNewGame(gamePlayerSet);

        // then
        verify(gamePlayerDao, times(6)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
        List<Unit> units = unitArgumentCaptor.getAllValues();

        // first player
        assertThat(units.get(0).getTerritory(), is(Territory.DENMARK));
        assertThat(units.get(2).getTerritory(), is(Territory.ICELAND));

        // second player
        assertThat(units.get(1).getTerritory(), is(Territory.FINLAND));
    }

    @Test
    public void testAllPlayersAreAssignedEqualNumberOfUnitsForOddNumberOfTerritories() {
        // given
        List<Territory> oddTerritories = defaultTerritories();
        oddTerritories.add(Territory.ICELAND);
        Set<GamePlayer> gamePlayerSet = defaultGamePlayerSet();
        setupMockForUnassignedTerritory();

        // when
        gameSetupService.initializeNewGame(gamePlayerSet);

        // then
        verify(gamePlayerDao, times(6)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
        List<Unit> units = unitArgumentCaptor.getAllValues();

        // second player
        assertThat(units.get(3).getTerritory(), is(Territory.UNASSIGNED_TERRITORY));
        assertThat(units.get(3).getStrength(), is(4));
    }

    @Test
    public void testOddTerritoryAssignedPlayerGetsOneMoreUnassignedUnit() {
        // given
        List<Territory> oddTerritories = defaultTerritories();
        oddTerritories.add(Territory.ICELAND);
        Set<GamePlayer> gamePlayerSet = defaultGamePlayerSet();
        setupMockForUnassignedTerritory();

        // when
        gameSetupService.initializeNewGame(gamePlayerSet);

        // then
        verify(gamePlayerDao, times(6)).updateUnitsToGamePlayer(gamePlayerIdArgumentCaptor.capture(), unitArgumentCaptor.capture());
        List<Unit> units = unitArgumentCaptor.getAllValues();

        // second player
        assertThat(units.get(5).getTerritory(), is(Territory.UNASSIGNED_TERRITORY));
        assertThat(units.get(5).getStrength(), is(4));
    }

    private Set<GamePlayer> defaultGamePlayerSet() {
        Set<GamePlayer> gamePlayerSet = new HashSet<>();
        gamePlayerSet.add(gamePlayerOne);
        gamePlayerSet.add(gamePlayerTwo);
        return gamePlayerSet;
    }

    private List<Territory> defaultTerritories() {
        List<Territory> territories = new ArrayList<>();
        territories.add(Territory.DENMARK);
        territories.add(Territory.FINLAND);
        when(gameTerritoryHandlerService.getShuffledTerritories()).thenReturn(territories);
        return territories;
    }

    private void setupMockForUnassignedTerritory() {
        when(gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerTwoId)).thenReturn(gamePlayerTwo);
        when(gamePlayerDao.getGamePlayerByGamePlayerId(gamePlayerOneId)).thenReturn(gamePlayerOne);
        gamePlayerTwo.setUnits(Arrays.asList(getUnassignedUnit()));
        gamePlayerOne.setUnits(Arrays.asList(getUnassignedUnit()));
    }

    private Unit getUnassignedUnit() {
        Unit unassignedUnit = new Unit();
        unassignedUnit.setStrength(0);
        unassignedUnit.setTerritory(Territory.UNASSIGNED_TERRITORY);
        return unassignedUnit;
    }
}