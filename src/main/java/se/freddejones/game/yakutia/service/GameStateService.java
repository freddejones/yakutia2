package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.TerritoryInformation;
import se.freddejones.game.yakutia.model.dto.TerritoryDTO;
import se.freddejones.game.yakutia.model.UnitId;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;

import java.util.List;

public interface GameStateService {

    public List<TerritoryInformation> getTerritoryInformationForActiveGame(GamePlayerId gamePlayerId);

}
