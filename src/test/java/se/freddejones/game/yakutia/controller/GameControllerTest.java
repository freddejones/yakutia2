package se.freddejones.game.yakutia.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.freddejones.game.yakutia.model.dto.GameInviteDTO;
import se.freddejones.game.yakutia.service.GameService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
public class GameControllerTest {

    private MockMvc mockMvc;
    private GameService gameService;

    @Before
    public void setup() {
        gameService = mock(GameService.class);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameController(gameService)).build();
    }

    @Test
    public void testThatControllerAcceptsBodyForAcceptGameInvite() throws Exception {
        GameInviteDTO gameInviteDTO = getGameInviteDTO();
        mockMvc.perform(post("/game/accept")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(gameInviteDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testThatControllerAcceptsBodyForDeclineGameInvite() throws Exception {
        GameInviteDTO gameInviteDTO = getGameInviteDTO();
        mockMvc.perform(post("/game/decline")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(gameInviteDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private GameInviteDTO getGameInviteDTO() {
        GameInviteDTO gameInviteDTO = new GameInviteDTO();
        gameInviteDTO.setGameId(1L);
        gameInviteDTO.setPlayerId(1L);
        return gameInviteDTO;
    }
}
