package se.freddejones.game.yakutia.controller;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.service.GameService;

import java.util.ArrayList;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest {

    private MockMvc mockMvc;
    private GameService gameService;

    @Before
    public void setup() {
        gameService = mock(GameService.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameController(gameService)).build();
    }

    @Test
    public void testGetGameForAPlayer() throws Exception {
        final ArrayList<TerritoryDTO> territoryDTOs = getYakutiaModels();
        when(gameService.getTerritoryInformationForActiveGame(anyLong(), anyLong())).thenReturn(territoryDTOs);
        mockMvc.perform(get("/game/get/1/game/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"landName\":\"testLand\",\"units\":6,\"ownedByPlayer\":true}]"));
    }

    @Test
    public void testGetWrongGameIdForPlayerResponse() throws Exception {
        final ArrayList<TerritoryDTO> territoryDTOs = getYakutiaModels();
        when(gameService.getTerritoryInformationForActiveGame(1L, 2L)).thenReturn(territoryDTOs);
        Assert.assertEquals("Not allowed to view this game",
                mockMvc.perform(get("/game/get/1/game/3"))
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    .andReturn().getResponse().getErrorMessage());
    }

    private ArrayList<TerritoryDTO> getYakutiaModels() {
        final TerritoryDTO testLand = new TerritoryDTO("testLand", 6, true);
        final ArrayList<TerritoryDTO> territoryDTOs = new ArrayList<TerritoryDTO>();
        territoryDTOs.add(testLand);
        return territoryDTOs;
    }


}
