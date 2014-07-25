package se.freddejones.game.yakutia.model.dto;


public class PlaceUnitDTO {

    private Long numberOfUnits;
    private String territory;
    private String unitType;
    private Long gamePlayerId;

    public Long getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Long numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(Long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    @Override
    public String toString() {
        return "PlaceUnitDTO{" +
                "numberOfUnits=" + numberOfUnits +
                ", territory=" + territory +
                ", unitType='" + unitType + '\'' +
                ", gamePlayerId=" + gamePlayerId +
                '}';
    }
}
