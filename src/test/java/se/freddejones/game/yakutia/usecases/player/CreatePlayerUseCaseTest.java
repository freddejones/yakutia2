package se.freddejones.game.yakutia.usecases.player;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;
import se.freddejones.game.yakutia.usecases.framework.UseCaseTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.convertDtoToByteArray;
import static se.freddejones.game.yakutia.usecases.framework.UseCaseBoilerplate.createPlayerDTO;

@Ignore
public class CreatePlayerUseCaseTest extends UseCaseTemplate {

    @Test
    public void UC_Player_01_createPlayer() throws Exception {
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
    public void UC_Player_02_updatePlayerName() throws Exception {
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
    public void UC_Player_03_fetchPlayer() throws Exception {
        TestdataHandler.loadDefaultTestdata();
        mockMvc.perform(get("/player/fetch/1"))
                .andDo(print())
                .andExpect(content().string("{\"playerName\":\"fidde_filth\",\"email\":\"freddejones@gmail.com\",\"playerId\":1}"));
    }

    @Test
    public void UC_Player_04_createPlayerAndFetch() throws Exception {
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
    public void UC_Player_05_updatePlayerNameAndFetchPlayer() throws Exception {
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
