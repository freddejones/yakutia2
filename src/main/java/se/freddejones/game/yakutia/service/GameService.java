package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.exception.*;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.*;

import java.util.List;

/**
 * User: Fredde
 * Date: 11/30/13 12:12 AM
 */
public interface GameService {

    public Long createNewGame(CreateGameDTO createGameDTO);
    public List<GameDTO> getGamesForPlayerById(Long playerid);
    public List<TerritoryDTO> getTerritoryInformationForActiveGame(Long playerId, Long gameId);
    public void setGameToStarted(Long gameId) throws NotEnoughPlayersException, TooManyPlayersException, CouldNotCreateGameException;
    public void setGameToFinished(Long gameId);

    public TerritoryDTO placeUnitAction(PlaceUnitUpdate placeUnitUpdate) throws NotEnoughUnitsException;
    public TerritoryDTO attackTerritoryAction(AttackActionUpdate attackActionUpdate) throws TerritoryNotConnectedException;
    public TerritoryDTO moveUnitsAction(PlaceUnitUpdate placeUnitUpdate);
    public GameStateModelDTO getGameStateModel(Long gameId, Long playerId);

    public TerritoryDTO getTerritoryInformationForTerritory(Territory territory, Long gameId, Long playerId);
}
