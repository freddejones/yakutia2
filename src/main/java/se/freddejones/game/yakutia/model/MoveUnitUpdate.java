package se.freddejones.game.yakutia.model;

import se.freddejones.game.yakutia.model.GamePlayerId;

public class MoveUnitUpdate {

    private Long fromTerritory;
    private Long toTerritiory;
    private Long numberOfUnits;
    private GamePlayerId gamePlayerId;

    public Long getFromTerritory() {
        return fromTerritory;
    }

    public void setFromTerritory(Long fromTerritory) {
        this.fromTerritory = fromTerritory;
    }

    public Long getToTerritiory() {
        return toTerritiory;
    }

    public void setToTerritiory(Long toTerritiory) {
        this.toTerritiory = toTerritiory;
    }

    public Long getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Long numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
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
                ", numberOfUnits=" + numberOfUnits +
                ", gamePlayerId=" + gamePlayerId +
                '}';
    }
}
