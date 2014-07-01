package se.freddejones.game.yakutia.service;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.AttackActionUpdate;
import se.freddejones.game.yakutia.model.MoveUnitUpdate;
import se.freddejones.game.yakutia.model.PlaceUnitUpdate;

public interface GameActionService {

    public void placeUnitAction(PlaceUnitUpdate placeUnitUpdate);
    public boolean attackTerritoryAction(AttackActionUpdate attackActionUpdate);
    public void moveUnitsAction(MoveUnitUpdate placeUnitUpdate);
    public void setActionsToDone(GamePlayerId gamePlayerId);

}
