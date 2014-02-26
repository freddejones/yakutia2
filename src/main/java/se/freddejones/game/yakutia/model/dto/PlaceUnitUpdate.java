package se.freddejones.game.yakutia.model.dto;

public class PlaceUnitUpdate {

    private int numberOfUnits;
    private String landArea;
    private Long gameId;
    private Long playerId;

    public PlaceUnitUpdate(int numberOfUnits, String landArea, Long gameId, Long playerId) {
        this.numberOfUnits = numberOfUnits;
        this.landArea = landArea;
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
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
}
