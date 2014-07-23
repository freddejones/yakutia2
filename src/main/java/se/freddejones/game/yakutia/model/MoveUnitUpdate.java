package se.freddejones.game.yakutia.model;

import java.util.Map;

public class MoveUnitUpdate {

    private Territory fromTerritory;
    private Territory toTerritiory;
    private Map<UnitType, Integer> unitsToMove;
    private GamePlayerId gamePlayerId;

    public Territory getFromTerritory() {
        return fromTerritory;
    }

    public void setFromTerritory(Territory fromTerritory) {
        this.fromTerritory = fromTerritory;
    }

    public Territory getToTerritiory() {
        return toTerritiory;
    }

    public void setToTerritiory(Territory toTerritiory) {
        this.toTerritiory = toTerritiory;
    }

    public Map<UnitType, Integer> getUnitsToMove() {
        return unitsToMove;
    }

    public void setUnitsToMove(Map<UnitType, Integer> unitsToMove) {
        this.unitsToMove = unitsToMove;
    }

    public GamePlayerId getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(GamePlayerId gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    @Override
    public String toString() {
        return "MoveUnitUpdate{" +
                "fromTerritory=" + fromTerritory +
                ", toTerritiory=" + toTerritiory +
                ", unitsToMove=" + unitsToMove +
                ", gamePlayerId=" + gamePlayerId +
                '}';
    }
}
