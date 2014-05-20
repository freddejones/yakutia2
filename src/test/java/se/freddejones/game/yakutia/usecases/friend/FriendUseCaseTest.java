package se.freddejones.game.yakutia.usecases.friend;

import org.junit.Test;
import org.springframework.http.MediaType;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;
import se.freddejones.game.yakutia.usecases.framework.UseCaseTemplate;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.convertDtoToByteArray;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.createFriendDTO;

public class FriendUseCaseTest extends UseCaseTemplate {

    @Test
    public void UC_F_01_InviteFriend() throws Exception {
        // given
        TestdataHandler.loadPlayersOnly();
        final byte[] request = convertDtoToByteArray(createFriendDTO());

        // when
        mockMvc.perform(post("/friend/invite/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void UC_F_02_InviteAndListNonFriends() throws Exception {
        // given
        TestdataHandler.loadPlayersOnly();
        final byte[] request = convertDtoToByteArray(createFriendDTO());

        // when
        mockMvc.perform(post("/friend/invite/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(status().is(200));

        mockMvc.perform((get("/friend/non/friends/1")))
                .andDo(print())
                // then
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(3, 4, 5)));

        mockMvc.perform((get("/friend/non/friends/2")))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(3,4,5)));
    }

    @Test
    public void UC_F_03_AcceptFriendAndFetch() throws Exception {
        // given
        TestdataHandler.loadDefaultTestdata();
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setPlayerId(3L);
        friendDTO.setFriendId(1L);
        final byte[] request = convertDtoToByteArray(friendDTO);

        // when
        mockMvc.perform(post("/friend/accept")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("{\"playerName\":\"fidde_filth\",\"email\":null,\"playerId\":null,\"friendId\":1,\"friendStatus\":\"ACCEPTED\"}"));

        // when
        mockMvc.perform(get("/friend/get/all/1"))
                .andDo(print())
                // then
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].friendId", containsInAnyOrder(3, 2)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(1, 1)));

        // when
        mockMvc.perform(get("/friend/get/all/3"))
                .andDo(print())
                // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].friendId", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(3)));
    }

    @Test
    public void UC_F_04_DeclineFriendAndFetch() throws Exception {
        // given
        TestdataHandler.loadDefaultTestdata();
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setPlayerId(3L);
        friendDTO.setFriendId(1L);
        final byte[] request = convertDtoToByteArray(friendDTO);

        // when
        mockMvc.perform(post("/friend/decline")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("true"));
        // when
        mockMvc.perform(get("/friend/get/all/3"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)));

        // when
        mockMvc.perform(get("/friend/get/all/1"))
                .andDo(print())
                // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].friendId", containsInAnyOrder(2)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(1)));
    }

    @Test
    public void UC_F_05_InviteAcceptAndFetch() throws Exception {
        // given
        TestdataHandler.loadPlayersOnly();
        final byte[] request = convertDtoToByteArray(createFriendDTO());

        // when
        mockMvc.perform(post("/friend/invite")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(status().isOk());

        // when
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setPlayerId(2L);
        friendDTO.setFriendId(1L);
        final byte[] acceptRequest = convertDtoToByteArray(friendDTO);

        // when
        mockMvc.perform(post("/friend/accept")
                .content(acceptRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(jsonPath("$.friendId", is(1)));

        // when
        mockMvc.perform(get("/friend/get/all/1"))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].friendId", containsInAnyOrder(2)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(1)));

        // when
        mockMvc.perform(get("/friend/get/all/2"))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].friendId", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(2)));

    }
}
