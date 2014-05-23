package se.freddejones.game.yakutia.usecases.game;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameInviteDTO;
import se.freddejones.game.yakutia.model.dto.InvitedPlayer;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;
import se.freddejones.game.yakutia.usecases.framework.UseCaseTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.convertDtoToByteArray;

public class CreateGamesUseCaseTest extends UseCaseTemplate {

    @Test
    public void UC_01_createGame() throws Exception {
        // given
        TestdataHandler.loadPlayersOnly();
        CreateGameDTO createGameDTO = createDefaultCreateGameDTO();
        byte[] request = convertDtoToByteArray(createGameDTO);

        // when
        mockMvc.perform(post("/game/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("1"));
    }

    @Test
    public void UC_02_createGameAndFetchAllGamesForPlayers() throws Exception {
        // given
        TestdataHandler.loadPlayersOnly();
        CreateGameDTO createGameDTO = createDefaultCreateGameDTO();
        byte[] request = convertDtoToByteArray(createGameDTO);

        // when
        mockMvc.perform(post("/game/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("1"));

        // when
        mockMvc.perform(get("/game/get/1"))
                .andDo(print())
                // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder("CREATED")))
                .andExpect(jsonPath("$[*].canStartGame", containsInAnyOrder(true)));

        // when
        mockMvc.perform(get("/game/get/2"))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder("CREATED")))
                .andExpect(jsonPath("$[*].canStartGame", containsInAnyOrder(false)));

        // when
        mockMvc.perform(get("/game/get/3"))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder("CREATED")))
                .andExpect(jsonPath("$[*].canStartGame", containsInAnyOrder(false)));
    }

    @Test
    public void UC_04_createGameAndAcceptGameInvites() throws Exception {
        // given
        TestdataHandler.loadPlayersOnly();
        CreateGameDTO createGameDTO = createDefaultCreateGameDTO();
        byte[] request = convertDtoToByteArray(createGameDTO);

        // when
        mockMvc.perform(post("/game/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("1"));

        request = convertDtoToByteArray(getGameInviteDTO(2L, 1L));
        mockMvc.perform(put("/game/accept")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        request = convertDtoToByteArray(getGameInviteDTO(3L, 1L));
        mockMvc.perform(put("/game/accept")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void UC_05_FetchStartedGameForAPlayer() throws Exception {

    }

    private CreateGameDTO createDefaultCreateGameDTO() {
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setGameName("tomtefräs");
        createGameDTO.setCreatedByPlayerId(1L);
        createGameDTO.setInvites(createInvitedPlayers(2L, 3L));
        return createGameDTO;
    }

    private GameInviteDTO getGameInviteDTO(Long playerId, Long gameId) {
        GameInviteDTO gameInviteDTO = new GameInviteDTO();
        gameInviteDTO.setPlayerId(playerId);
        gameInviteDTO.setGameId(gameId);
        return gameInviteDTO;
    }

    private List<InvitedPlayer> createInvitedPlayers(Long... ids) {
        List<InvitedPlayer> invitedPlayers = new ArrayList<>();
        for (Long id : ids) {
            InvitedPlayer invitedPlayer = new InvitedPlayer();
            invitedPlayer.setId(id);
            invitedPlayers.add(invitedPlayer);
        }
        return invitedPlayers;
    }
}
