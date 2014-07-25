package se.freddejones.game.yakutia.usecases;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import se.freddejones.game.yakutia.TestDataSets;
import se.freddejones.game.yakutia.controller.GameController;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.convertDtoToByteArray;

public class CreateGamesUseCaseTest extends UseCaseConfiguration {

    @Autowired
    GameController gameController;

    @Before
    public void setUp() throws Exception {
        mockMvc = standaloneSetup(gameController).build();
        TestdataHandler.resetAndRebuild();
    }

    @Test
    public void UC_01_createGame() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYERS_ONLY_XML);
        CreateGameDTO createGameDTO = createDefaultCreateGameDTO();
        byte[] request = convertDtoToByteArray(createGameDTO);

        // when
        mockMvc.perform(post("/game/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(content().string("1"));
    }

    @Test
    public void UC_02_createGameAndFetchAllGamesForPlayers() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYERS_ONLY_XML);
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
        mockMvc.perform(get("/game/player/1"))
                .andDo(print())
                // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder("CREATED")))
                .andExpect(jsonPath("$[*].canStartGame", containsInAnyOrder(true)));

        // when
        mockMvc.perform(get("/game/player/2"))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder("CREATED")))
                .andExpect(jsonPath("$[*].canStartGame", containsInAnyOrder(false)));

        // when
        mockMvc.perform(get("/game/player/3"))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder("CREATED")))
                .andExpect(jsonPath("$[*].canStartGame", containsInAnyOrder(false)));
    }

    @Test
    public void UC_04_createGameAndAcceptGameInvitesAndStartGame() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYERS_ONLY_XML);
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
        mockMvc.perform(post("/game/accept/2"))
                .andDo(print())
                // then
                .andExpect(status().isOk());

        // when
        mockMvc.perform(post("/game/accept/3"))
                .andDo(print())
                // then
                .andExpect(status().isOk());

        // when
        mockMvc.perform(put("/game/start/1"))
                .andDo(print())
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void UC_05_startGameForThreePlayers() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.GAME_GAMEPLAYER_PLAYERS_XML);
        mockMvc.perform(put("/game/start/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private CreateGameDTO createDefaultCreateGameDTO() {
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setGameName("tomtefräs");
        createGameDTO.setCreatedByPlayerId(1L);
        createGameDTO.setInvites(createInvitedPlayers(2L, 3L));
        return createGameDTO;
    }

    private List<Long> createInvitedPlayers(Long... ids) {
        List<Long> invitedPlayers = new ArrayList<>();
        for (Long id : ids) {;
            invitedPlayers.add(id);
        }
        return invitedPlayers;
    }
}
