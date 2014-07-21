package se.freddejones.game.yakutia.service;
import se.freddejones.game.yakutia.model.*;

public interface GameActionService {

    public void placeUnitAction(PlaceUnitUpdate placeUnitUpdate);
    public void attackTerritoryAction(AttackActionUpdate attackActionUpdate);
    public void moveUnitsAction(MoveUnitUpdate placeUnitUpdate);
    public void setActionsToDone(GamePlayerId gamePlayerId);

}
