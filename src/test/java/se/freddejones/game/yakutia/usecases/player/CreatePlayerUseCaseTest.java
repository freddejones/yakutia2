package se.freddejones.game.yakutia.usecases.player;

import com.google.inject.Inject;
import liquibase.exception.LiquibaseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.freddejones.game.yakutia.controller.PlayerController;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;
import se.freddejones.game.yakutia.usecases.framework.FullApplicationContextConfiguration;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import javax.annotation.Resource;

import java.sql.SQLException;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.convertDtoToByteArray;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.createPlayerDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
//@ContextConfiguration(locations = {FullApplicationContextConfiguration.class})
@ContextConfiguration(locations = {
        "classpath:applicationTestContext.xml",
        "classpath:hibernateTestContext.xml"})
public class CreatePlayerUseCaseTest {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        TestdataHandler.resetAndRebuild();
    }

    @Test
    public void UC_001_createPlayer() throws Exception {
        // given
        final byte[] request = convertDtoToByteArray(createPlayerDTO());

        // when
        mockMvc.perform(post("/player/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("1"));
    }

    @Test
    public void UC_002_updatePlayerName() throws Exception {
        // given
        TestdataHandler.loadDefaultTestdata();
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerName("ny namnet");
        playerDTO.setPlayerId(1L);
        final byte[] request = convertDtoToByteArray(playerDTO);

        // when
        mockMvc.perform(post("/player/update/name")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("1"));
    }

    @Test
    public void UC_003_fetchPlayer() throws Exception {
        TestdataHandler.loadDefaultTestdata();
        mockMvc.perform(get("/player/fetch/1"))
                .andDo(print())
                .andExpect(content().string("{\"playerName\":\"fidde_filth\",\"email\":\"freddejones@gmail.com\",\"playerId\":1}"));
    }

    @Test
    public void UC_004_createPlayerAndFetch() throws Exception {
        // given
        final byte[] request = convertDtoToByteArray(createPlayerDTO());

        // when
        mockMvc.perform(post("/player/create/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // when
        mockMvc.perform(get("/player/fetch/1"))
                .andDo(print())
                // then
                .andExpect(content().string("{\"playerName\":\"tomten\",\"email\":\"fräs@frässish\",\"playerId\":1}"));

    }

    @Test
    public void UC_005_updatePlayerNameAndFetchPlayer() throws Exception {
        // given
        TestdataHandler.loadDefaultTestdata();
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerName("ny namnet");
        playerDTO.setPlayerId(3L);
        final byte[] request = convertDtoToByteArray(playerDTO);

        // when
        mockMvc.perform(post("/player/update/name")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then
                .andExpect(content().string("3"));

        // when
        mockMvc.perform(get("/player/fetch/3"))
                .andDo(print())
                // then
                .andExpect(content().string("{\"playerName\":\"ny namnet\",\"email\":\"janek@gmail.com\",\"playerId\":3}"));
    }

}
