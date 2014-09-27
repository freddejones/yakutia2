package se.freddejones.game.yakutia.usecases;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.HibernateConfigForTest;
import se.freddejones.game.yakutia.TestDataSets;
import se.freddejones.game.yakutia.application.BattleEngineCalculator;
import se.freddejones.game.yakutia.application.Dice;
import se.freddejones.game.yakutia.controller.FriendController;
import se.freddejones.game.yakutia.controller.PlayerController;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.FriendInviteDTO;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.service.FriendService;
import se.freddejones.game.yakutia.service.PlayerService;
import se.freddejones.game.yakutia.service.impl.DefaultFriendService;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import java.util.List;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.convertDtoToByteArray;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.createFriendDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class FriendUseCaseTest {

    @Configuration
    @Import(HibernateConfigForTest.class)
    @ComponentScan(basePackageClasses = {PlayerController.class, PlayerService.class, PlayerDao.class, BattleEngineCalculator.class, Dice.class})
    static class TestConfiguration {}

    private MockMvc mockMvc;

    @Autowired
    FriendController friendController;

    @Autowired
    PlayerFriendDao playerFriendDao;
    @Autowired
    PlayerDao playerDao;

    @Before
    public void setUp() throws Exception {
        mockMvc = standaloneSetup(friendController).build();
        TestdataHandler.resetAndRebuild();
    }

    @Test
    public void UCF_01_FetchAllAcceptedFriends() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);

        // when
        mockMvc.perform(get("/friend/accepted/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(2)))
                .andExpect(status().isOk());
    }

    @Test
    public void UCF_02_FetchAllInvites() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);

        // when
        mockMvc.perform(get("/friend/invites/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void UCF_03_InviteFriend() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYERS_ONLY_XML);
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
    public void UCF_04_InviteFriendAndFetchThatInviteForTheOtherPlayer() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYERS_ONLY_XML);
        final byte[] request = convertDtoToByteArray(createFriendDTO());

        // when
        mockMvc.perform(post("/friend/invite/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                        // then
                .andExpect(status().isOk());

        // when
        mockMvc.perform(get("/friend/invites/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                        // then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(1)))
                .andExpect(status().isOk());
    }


    @Test
    public void UCF_05_AcceptFriendAndFetch() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);
        FriendInviteDTO friendInviteDTO = new FriendInviteDTO();
        friendInviteDTO.setPlayerId(3L);
        friendInviteDTO.setFriendId(1L);
        final byte[] request = convertDtoToByteArray(friendInviteDTO);

        // when
        mockMvc.perform(post("/friend/accept")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(status().isOk());

        // when
        mockMvc.perform(get("/friend/accepted/1"))
                .andDo(print())
                // then
                .andExpect(jsonPath("$[*].playerId", containsInAnyOrder(3, 2)));
    }

    @Ignore
    @Test
    public void UC_F_04_DeclineFriendAndFetch() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);
        FriendInviteDTO friendInviteDTO = new FriendInviteDTO();
        friendInviteDTO.setPlayerId(3L);
        friendInviteDTO.setFriendId(1L);
        final byte[] request = convertDtoToByteArray(friendInviteDTO);

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

    @Ignore
    @Test
    public void UCF_02_InviteAndListNonFriends() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYERS_ONLY_XML);
        final byte[] request = convertDtoToByteArray(createFriendDTO());

        // when
        mockMvc.perform(post("/friend/invite/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                        // then
                .andExpect(status().isOk());

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

    @Ignore
    @Test
    public void UC_F_05_InviteAcceptAndFetch() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYERS_ONLY_XML);
        final byte[] request = convertDtoToByteArray(createFriendDTO());

        // when
        mockMvc.perform(post("/friend/invite")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(status().isOk());

        // when
        FriendInviteDTO friendInviteDTO = new FriendInviteDTO();
        friendInviteDTO.setPlayerId(2L);
        friendInviteDTO.setFriendId(1L);
        final byte[] acceptRequest = convertDtoToByteArray(friendInviteDTO);

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
