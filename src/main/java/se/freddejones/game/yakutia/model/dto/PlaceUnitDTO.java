package se.freddejones.game.yakutia.model.dto;


public class PlaceUnitDTO {

    private Long numberOfUnits;
    private Long territory;
    private Long gamePlayerId;

    public Long getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Long numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public Long getTerritory() {
        return territory;
    }

    public void setTerritory(Long territory) {
        this.territory = territory;
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
                ", gamePlayerId=" + gamePlayerId +
                '}';
    }
}
