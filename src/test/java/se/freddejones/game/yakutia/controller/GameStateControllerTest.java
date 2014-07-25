package se.freddejones.game.yakutia.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.service.GameActionService;
import se.freddejones.game.yakutia.service.GameStateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameStateControllerTest {

    private MockMvc mockMvc;
    private GameStateService gameStateService;
    private GamePlayerId gamePlayerId;

    @Before
    public void setup() {
        gamePlayerId = new GamePlayerId(1L);
        gameStateService = mock(GameStateService.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameStateController(
                gameStateService)).build();
    }

    @Test
    public void testGettingTerritoryInformatationGET() throws Exception {
        when(gameStateService.getTerritoryInformationForActiveGame(gamePlayerId)).thenReturn(getTerritories());
        mockMvc.perform(get("/state/gameplayer/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"territory\":\"DENMARK\",\"units\":{\"SOLDIER\":1},\"gamePlayerId\":1}]"));
    }

    private List<TerritoryInformation> getTerritories() {
        Map<UnitType, Integer> units = new HashMap<>();
        units.put(UnitType.SOLDIER, 1);
        TerritoryInformation territoryInformation = new TerritoryInformation(Territory.DENMARK, units, gamePlayerId);
        List<TerritoryInformation> territoryInformations = new ArrayList<>();
        territoryInformations.add(territoryInformation);
        return territoryInformations;
    }
}