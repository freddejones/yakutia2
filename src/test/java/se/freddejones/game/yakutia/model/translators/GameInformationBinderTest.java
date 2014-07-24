package se.freddejones.game.yakutia.model.translators;

import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryInformation;
import se.freddejones.game.yakutia.model.UnitType;
import se.freddejones.game.yakutia.model.dto.TerritoryDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GameInformationBinderTest {

    private GameInformationBinder gameInformationBinder;
    private GamePlayerId gamePLayerId;

    @Before
    public void setUp() throws Exception {
        gameInformationBinder = new GameInformationBinder();
        gamePLayerId = new GamePlayerId(1L);
    }

    @Test
    public void testMapAllValues() {
        // given
        TerritoryInformation territoryInformation = getTerritoryInformation();

        // when
        TerritoryDTO territoryDTO = gameInformationBinder.bind(territoryInformation);

        // then
        assertThat(territoryDTO.getTerritory(), is("DENMARK"));
        assertThat(territoryDTO.getGamePlayerId(), is(1L));
        assertThat(territoryDTO.getUnits().size(), is(3));
    }

    private TerritoryInformation getTerritoryInformation() {
        Map<UnitType, Integer> units = new HashMap<>();
        units.put(UnitType.SOLDIER, 1);
        units.put(UnitType.TANK, 1);
        units.put(UnitType.AIRPLANE, 1);
        return new TerritoryInformation(Territory.DENMARK, units, gamePLayerId);
    }

    @Test
    public void testMapList() {
        // given
        List<TerritoryInformation> territoryInformations = new ArrayList<>();
        territoryInformations.add(getTerritoryInformation());
        territoryInformations.add(getTerritoryInformation());

        // when
        List<TerritoryDTO> dtos = gameInformationBinder.bind(territoryInformations);

        // then
        assertThat(dtos.size(), is(2));
    }
}