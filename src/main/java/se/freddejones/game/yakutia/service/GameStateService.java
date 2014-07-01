package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.UnitId;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;

import java.util.List;

public interface GameStateService {

    public List<TerritoryDTO> getTerritoryInformationForActiveGame(GamePlayerId gamePlayerId);
    public GameStateModelDTO getGameStateModel(GamePlayerId gamePlayerId);
    public TerritoryDTO getTerritoryInformation(UnitId unitId, GamePlayerId gamePlayerId);

}
