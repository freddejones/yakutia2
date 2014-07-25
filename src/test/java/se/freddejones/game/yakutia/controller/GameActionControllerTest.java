package se.freddejones.game.yakutia.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.MoveUnitUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.dto.AttackActionDTO;
import se.freddejones.game.yakutia.model.dto.MoveUnitUpdateDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitDTO;
import se.freddejones.game.yakutia.service.GameActionService;

import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameActionControllerTest {

    private MockMvc mockMvc;
    private GameActionService gameActionService;

    @Before
    public void setup() {
        gameActionService = mock(GameActionService.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameActionController(gameActionService)).build();
    }

    @Test
    public void testControlleToAcceptPlaceUnitsOperationForPOST() throws Exception {
        PlaceUnitDTO placeUnitDTO = new PlaceUnitDTO();
        placeUnitDTO.setUnitType("SOLDIER");
        placeUnitDTO.setNumberOfUnits(1L);
        placeUnitDTO.setGamePlayerId(1L);
        placeUnitDTO.setTerritory("SWEDEN");
        mockMvc.perform(post("/game/action/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(placeUnitDTO)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(gameActionService, times(1)).placeUnitAction(any(PlaceUnitUpdate.class));
    }

    @Test
    public void testControlleToAcceptAttackOperationForPOST() throws Exception {
        AttackActionDTO attackActionDTO = new AttackActionDTO();
        HashMap<String, Integer> attackingUnits = new HashMap<String, Integer>();
        attackingUnits.put("TANK", 5);
        attackActionDTO.setAttackingUnits(attackingUnits);
        attackActionDTO.setAttackingGamePlayerId(1L);
        attackActionDTO.setDefendingGamePlayerId(2L);
        attackActionDTO.setTerritoryAttackSrc("SWEDEN");
        attackActionDTO.setTerritoryAttackDst("DENMARK");
        mockMvc.perform(post("/game/action/attack")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(attackActionDTO)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(gameActionService, times(1)).attackTerritoryAction(any(AttackActionUpdate.class));
    }

    @Test
    public void testControlleToAcceptMoveUnitOperationForPOST() throws Exception {
        MoveUnitUpdateDTO moveUnitUpdateDTO = new MoveUnitUpdateDTO();
        HashMap<String, Integer> units = new HashMap<String, Integer>();
        units.put("TANK", 5);
        moveUnitUpdateDTO.setUnits(units);
        moveUnitUpdateDTO.setGamePlayerId(1L);
        moveUnitUpdateDTO.setFromTerritory("SWEDEN");
        moveUnitUpdateDTO.setToTerritory("DENMARK");
        mockMvc.perform(post("/game/action/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(moveUnitUpdateDTO)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(gameActionService, times(1)).moveUnitsAction(any(MoveUnitUpdate.class));
    }
}