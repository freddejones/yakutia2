package se.freddejones.game.yakutia.model.translators;

import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.dto.AttackActionDTO;
import se.freddejones.game.yakutia.model.dto.MoveUnitUpdateDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitDTO;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GameActionBinderTest {

    private GameActionBinder gameActionBinder;

    @Before
    public void setUp() throws Exception {
        gameActionBinder = new GameActionBinder();
    }

    @Test
    public void testTranslateAllValuesForPlaceUnitDTO() {
        // given
        PlaceUnitDTO placeUnitDTO = new PlaceUnitDTO();
        placeUnitDTO.setGamePlayerId(1L);
        placeUnitDTO.setNumberOfUnits(1L);
        placeUnitDTO.setTerritory("SWEDEN");
        placeUnitDTO.setUnitType("TANK");

        // when
        PlaceUnitUpdate placeUnitUpdate = gameActionBinder.bind(placeUnitDTO);

        // then
        assertThat(placeUnitUpdate.getGamePlayerId(), is(new GamePlayerId(1L)));
        assertThat(placeUnitUpdate.getNumberOfUnits(), is(1));
        assertThat(placeUnitUpdate.getTerritory(), is(Territory.SWEDEN));
        assertThat(placeUnitUpdate.getUnitType(), is(UnitType.TANK));
    }

    @Test
    public void testTranslateAllValuesForAttackActionDTO() {
        // given
        AttackActionDTO attackActionDTO = new AttackActionDTO();
        attackActionDTO.setTerritoryAttackSrc("SWEDEN");
        attackActionDTO.setTerritoryAttackDst("DENMARK");
        attackActionDTO.setAttackingGamePlayerId(1L);
        attackActionDTO.setDefendingGamePlayerId(2L);
        Map<String, Integer> units = new HashMap<>();
        units.put("TANK", 1);
        attackActionDTO.setAttackingUnits(units);

        // when
        AttackActionUpdate attackActionUpdate = gameActionBinder.bind(attackActionDTO);

        // then
        assertThat(attackActionUpdate.getGamePlayerId(), is(new GamePlayerId(1L)));
        assertThat(attackActionUpdate.getDefendingGamePlayerId(), is(new GamePlayerId(2L)));
        assertThat(attackActionUpdate.getAttackingNumberOfUnits().size(), is(1));
        assertThat(attackActionUpdate.getTerritoryAttackSrc(), is(Territory.SWEDEN));
        assertThat(attackActionUpdate.getTerritoryAttackDest(), is(Territory.DENMARK));
    }

    @Test
    public void testTranslateAllValuesForMoveUnitActionDTO() {
        // given
        MoveUnitUpdateDTO moveUnitUpdateDTO = new MoveUnitUpdateDTO();
        moveUnitUpdateDTO.setFromTerritory("SWEDEN");
        moveUnitUpdateDTO.setToTerritory("DENMARK");
        moveUnitUpdateDTO.setGamePlayerId(1L);
        Map<String, Integer> units = new HashMap<>();
        units.put("TANK", 1);
        moveUnitUpdateDTO.setUnits(units);

        // when
        MoveUnitUpdate moveUnitUpdate = gameActionBinder.bind(moveUnitUpdateDTO);

        // then
        assertThat(moveUnitUpdate.getGamePlayerId(), is(new GamePlayerId(1L)));
        assertThat(moveUnitUpdate.getUnitsToMove().size(), is(1));
        assertThat(moveUnitUpdate.getFromTerritory(), is(Territory.SWEDEN));
        assertThat(moveUnitUpdate.getToTerritiory(), is(Territory.DENMARK));
    }
}