package se.freddejones.game.yakutia.model.dto;

import se.freddejones.game.yakutia.model.UnitType;

import java.io.Serializable;
import java.util.Map;

public class TerritoryDTO implements Serializable {

    private String territory;
    private Map<UnitType, Integer> units;
    private Long gamePlayerId;

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public Map<UnitType, Integer> getUnits() {
        return units;
    }

    public void setUnits(Map<UnitType, Integer> units) {
        this.units = units;
    }

    public Long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(Long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }
}
