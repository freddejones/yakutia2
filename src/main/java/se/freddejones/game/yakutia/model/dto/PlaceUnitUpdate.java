package se.freddejones.game.yakutia.model.dto;

public class PlaceUnitUpdate {

    private Integer numberOfUnits;
    private String territory;
    private Long gameId;
    private Long playerId;

    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "PlaceUnitUpdate{" +
                "numberOfUnits=" + numberOfUnits +
                ", territory='" + territory + '\'' +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                '}';
    }
}
