package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;

public interface GameActionService {

    public TerritoryDTO placeUnitAction(PlaceUnitUpdate placeUnitUpdate) throws NotEnoughUnitsException;
    public TerritoryDTO attackTerritoryAction(AttackActionUpdate attackActionUpdate) throws TerritoryNotConnectedException;
    public TerritoryDTO moveUnitsAction(PlaceUnitUpdate placeUnitUpdate);

}
