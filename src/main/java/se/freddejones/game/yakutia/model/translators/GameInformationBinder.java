package se.freddejones.game.yakutia.model.translators;

import se.freddejones.game.yakutia.model.TerritoryInformation;
import se.freddejones.game.yakutia.model.dto.TerritoryDTO;

import java.util.ArrayList;
import java.util.List;

public class GameInformationBinder {

    public TerritoryDTO bind(TerritoryInformation territoryInformation) {
        TerritoryDTO territoryDTO = new TerritoryDTO();
        territoryDTO.setTerritory(territoryInformation.getTerritory().toString());
        territoryDTO.setGamePlayerId(territoryInformation.getGamePlayerId().getGamePlayerId());
        territoryDTO.setUnits(territoryInformation.getUnits());
        return territoryDTO;
    }

    public List<TerritoryDTO> bind(List<TerritoryInformation> territoryInformationList) {
        List<TerritoryDTO> territoryDTOs = new ArrayList<>();
        for (TerritoryInformation territoryInformation : territoryInformationList) {
            territoryDTOs.add(bind(territoryInformation));
        }
        return territoryDTOs;
    }

}
