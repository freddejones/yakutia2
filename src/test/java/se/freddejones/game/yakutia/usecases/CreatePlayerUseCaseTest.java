package se.freddejones.game.yakutia.usecases;

import org.junit.Before;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.HibernateConfig;
import se.freddejones.game.yakutia.TestDataSets;
import se.freddejones.game.yakutia.application.BattleEngineCalculator;
import se.freddejones.game.yakutia.application.Dice;
import se.freddejones.game.yakutia.controller.PlayerController;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;
import se.freddejones.game.yakutia.service.PlayerService;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.convertDtoToByteArray;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.createPlayerDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class CreatePlayerUseCaseTest {

    @Configuration
    @Import(HibernateConfig.class)
    @ComponentScan(basePackageClasses = {PlayerController.class, PlayerService.class, PlayerDao.class, BattleEngineCalculator.class, Dice.class})
    static class TestConfiguration {}

    private MockMvc mockMvc;

    @Autowired
    PlayerController playerController;

    @Before
    public void setUp() throws Exception {
        mockMvc = standaloneSetup(playerController).build();
        TestdataHandler.resetAndRebuild();
    }


    @Test
    public void UC_Player_01_createPlayer() throws Exception {
        // given
        final byte[] request = convertDtoToByteArray(createPlayerDTO());

        // when
        mockMvc.perform(post("/player/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(content().string("1"));
    }

    @Test
    public void UC_Player_02_updatePlayerName() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerName("ny namnet");
        playerDTO.setPlayerId(1L);
        final byte[] request = convertDtoToByteArray(playerDTO);

        // when
        mockMvc.perform(put("/player/update/name")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(content().string("1"));
    }

    @Test
    public void UC_Player_03_fetchPlayer() throws Exception {
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);
        mockMvc.perform(get("/player/fetch/1"))
                .andExpect(content().string("{\"playerName\":\"fidde_filth\",\"email\":\"freddejones@gmail.com\",\"playerId\":1}"));
    }

    @Test
    public void UC_Player_04_createPlayerAndFetch() throws Exception {
        // given
        final byte[] request = convertDtoToByteArray(createPlayerDTO());

        // when
        MvcResult result = mockMvc.perform(post("/player/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String id = result.getResponse().getContentAsString();

        // when
        mockMvc.perform(get("/player/fetch/"+id))
                // then
                .andExpect(content().string("{\"playerName\":\"tomten\",\"email\":\"fräs@frässish\",\"playerId\":1}"));
    }

    @Test
    public void UC_Player_05_updatePlayerNameAndFetchPlayer() throws Exception {
        // given
        TestdataHandler.loadChangeSet(TestDataSets.PLAYER_PLAYERFRIEND_XML);
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerName("ny namnet");
        playerDTO.setPlayerId(3L);
        final byte[] request = convertDtoToByteArray(playerDTO);

        // when
        mockMvc.perform(put("/player/update/name")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(content().string("3"));

        // when
        mockMvc.perform(get("/player/fetch/3"))
                // then
                .andExpect(content().string("{\"playerName\":\"ny namnet\",\"email\":\"janek@gmail.com\",\"playerId\":3}"));
    }

}
