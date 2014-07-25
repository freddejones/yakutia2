package se.freddejones.game.yakutia.model.dto;

import java.util.Map;

public class MoveUnitUpdateDTO {

    private String fromTerritory;
    private String toTerritory;
    private Map<String,Integer> units;
    private Long gamePlayerId;

    public String getFromTerritory() {
        return fromTerritory;
    }

    public void setFromTerritory(String fromTerritory) {
        this.fromTerritory = fromTerritory;
    }

    public String getToTerritory() {
        return toTerritory;
    }

    public void setToTerritory(String toTerritory) {
        this.toTerritory = toTerritory;
    }

    public Map<String, Integer> getUnits() {
        return units;
    }

    public void setUnits(Map<String, Integer> units) {
        this.units = units;
    }

    public Long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(Long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }
}
