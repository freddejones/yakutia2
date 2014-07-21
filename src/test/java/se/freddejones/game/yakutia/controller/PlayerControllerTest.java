package se.freddejones.game.yakutia.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.service.PlayerService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PlayerControllerTest {

    private PlayerService playerService;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        playerService = mock(PlayerService.class);
        PlayerController playerController = new PlayerController(playerService);
        mockMvc = standaloneSetup(playerController).build();
    }

    @Test
    public void testCreatePlayerIsAcceptingPOST() throws Exception {
        String createPlayer = "{\"playerName\":\"test\",\"email\":\"epost\"}";
        when(playerService.createNewPlayer(any(Player.class))).thenReturn(new PlayerId(1L));
        mockMvc.perform(post("/player/create")
                .content(createPlayer).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(playerService, times(1)).createNewPlayer(any(Player.class));
    }

    @Test
    public void testUpdatePlayerNameSupportPUT() throws Exception {
        String updatePlayerName = "{\"playerName\":\"test\",\"email\":\"epost\"}";
        when(playerService.updatePlayerName(any(PlayerId.class), anyString())).thenReturn(new PlayerId(1L));
        mockMvc.perform(put("/player/update/name")
                .content(updatePlayerName).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(playerService, times(1)).updatePlayerName(any(PlayerId.class), anyString());
    }

    @Test
    public void testFetchPlayerSupportGET() throws Exception {
        when(playerService.getPlayerById(any(PlayerId.class))).thenReturn(new Player());
        mockMvc.perform(get("/player/fetch/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(playerService, times(1)).getPlayerById(any(PlayerId.class));
    }
}