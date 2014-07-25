package se.freddejones.game.yakutia.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.GameId;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.service.GamePlayerStatusHandler;
import se.freddejones.game.yakutia.service.GameService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest {

    private MockMvc mockMvc;
    private GameService gameService;
    private GamePlayerStatusHandler gamePlayerStatusHandler;
    private PlayerId playerId;

    @Before
    public void setup() {
        playerId = new PlayerId(1L);
        gameService = mock(GameService.class);
        gamePlayerStatusHandler = mock(GamePlayerStatusHandler.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameController(gameService, gamePlayerStatusHandler)).build();
    }

    @Test
    public void testCanCreateGamePOST() throws Exception {

        // given
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setInvites(Arrays.asList(2L));
        createGameDTO.setGameName("test");
        createGameDTO.setCreatedByPlayerId(1L);
        when(gameService.createNewGame(any(CreateGame.class))).thenReturn(new GameId(1L));

        // when
        mockMvc.perform(post("/game/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(createGameDTO)))
                // then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }

    @Test
    public void testCanFetchGameGET() throws Exception {
        List<Game> gamesToReturn = Arrays.asList(createGameObject(false));
        when(gameService.getGamesForPlayerById(playerId)).thenReturn(gamesToReturn);
        mockMvc.perform(get("/game/player/" + playerId.getPlayerId()))
                .andExpect(content().string(containsString("\"id\":1,\"name\":\"tomte\",\"status\":\"ONGOING\"")));
    }

    @Test
    public void testThatControllerForAcceptGameInvitePOST() throws Exception {
        mockMvc.perform(post("/game/accept/1"))
                .andExpect(status().isOk());
        verify(gamePlayerStatusHandler, times(1)).acceptGameInvite(new GamePlayerId(1L));
    }

    @Test
    public void testThatControllerAcceptsBodyForDeclineGameInvite() throws Exception {
        mockMvc.perform(post("/game/decline/1"))
                .andExpect(status().isOk());
        verify(gamePlayerStatusHandler, times(1)).declineGameInvite(new GamePlayerId(1L));
    }

    private Game createGameObject(boolean activePlayerTurn) {
        Game game = new Game();
        game.setGameId(1L);
        game.setCreationTime(new LocalDate().toDate());
        game.setName("tomte");
        game.setGameCreatorPlayerId(1L);
        game.setGameStatus(GameStatus.ONGOING);
        Set<GamePlayer> gamePlayerSet = new HashSet<>();
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setPlayerId(playerId.getPlayerId());
        gamePlayer.setActivePlayerTurn(activePlayerTurn);
        gamePlayerSet.add(gamePlayer);
        game.setPlayers(gamePlayerSet);
        return game;
    }
}
