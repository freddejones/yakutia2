package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;

import java.util.List;

public interface GameStateService {

    public List<TerritoryDTO> getTerritoryInformationForActiveGame(Long playerId, Long gameId);
    public GameStateModelDTO getGameStateModel(Long gameId, Long playerId);
    public TerritoryDTO getTerritoryInformationForTerritory(Territory territory, Long gameId, Long playerId);

}
