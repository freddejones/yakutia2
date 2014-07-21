package se.freddejones.game.yakutia.model;

public class PlaceUnitUpdate {

    private Integer numberOfUnits;
    private Territory territory;
    private UnitType unitType;
    private GamePlayerId gamePlayerId;

    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public Territory getTerritory() {
        return territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public GamePlayerId getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(GamePlayerId gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    @Override
    public String toString() {
        return "PlaceUnitUpdate{" +
                "numberOfUnits=" + numberOfUnits +
                ", territory=" + territory +
                ", unitType=" + unitType +
                ", gamePlayerId=" + gamePlayerId +
                '}';
    }
}
