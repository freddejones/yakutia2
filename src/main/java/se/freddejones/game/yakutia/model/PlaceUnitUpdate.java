package se.freddejones.game.yakutia.model;

public class PlaceUnitUpdate {

    private Long numberOfUnits;
    private UnitId territory;
    private GamePlayerId gamePlayerId;

    public Long getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Long numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public UnitId getTerritory() {
        return territory;
    }

    public void setTerritory(UnitId territory) {
        this.territory = territory;
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
                ", gamePlayerId=" + gamePlayerId +
                '}';
    }
}
