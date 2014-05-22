package se.freddejones.game.yakutia.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.service.GameService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameStateControllerTest {

    private MockMvc mockMvc;
    private GameService gameService;

    @Before
    public void setup() {
        gameService = mock(GameService.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameStateController(gameService)).build();
    }

    @Test
    public void testPlaceUnitUpdateAccepted() throws Exception {
        TerritoryDTO territoryDTO = new TerritoryDTO("TOMTE", 1, true);
        when(gameService.placeUnitAction(any(PlaceUnitUpdate.class))).thenReturn(territoryDTO);
        assertThat(mockMvc.perform(post("/state/perform/place/unit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"territory\":\"test\", \"gameId\":\"1\", \"playerId\":\"1\", \"numberOfUnits\":\"5\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), is("{\"landName\":\"TOMTE\",\"units\":1,\"ownedByPlayer\":true}"));
    }

    @Test
    public void testPlaceUnitUpdateExceptionThrown() throws Exception {
        when(gameService.placeUnitAction(any(PlaceUnitUpdate.class))).thenThrow(NotEnoughUnitsException.class);
        mockMvc.perform(post("/state/perform/place/unit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"territory\":\"test\", \"gameId\":\"1\", \"playerId\":\"1\", \"numberOfUnits\":\"5\"}"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAttackTerritoryOK() throws Exception {
        TerritoryDTO territoryDTO = new TerritoryDTO("TOMTE", 1, true);
        when(gameService.attackTerritoryAction(any(AttackActionUpdate.class))).thenReturn(territoryDTO);
        assertThat(mockMvc.perform(post("/state/perform/attack/territory")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"territoryAttackDest\":\"NORWAY\", \"territoryAttackSrc\":\"SWEDEN\", \"gameId\":\"1\", \"playerId\":\"1\", \"attackingNumberOfUnits\":\"5\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), is("{\"landName\":\"TOMTE\",\"units\":1,\"ownedByPlayer\":true}"));
    }

    @Test
    public void testGetTerritoryStateInformation() throws Exception {
        TerritoryDTO territoryDTO = new TerritoryDTO("SWEDEN", 1, true);
        when(gameService.getTerritoryInformationForTerritory(Territory.SWEDEN, 1L, 1L)).thenReturn(territoryDTO);
        String response = mockMvc.perform(get("/state/territory/1/1/sweden")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(response, containsString("{\"landName\":\"SWEDEN\",\"units\":1,\"ownedByPlayer\":true}"));
    }
}