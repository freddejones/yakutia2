package se.freddejones.game.yakutia.model;

import java.util.Map;

public class TerritoryInformation {

    private final Territory territory;
    private final Map<UnitType, Integer> units;
    private final GamePlayerId gamePlayerId;

    public TerritoryInformation(Territory territory, Map<UnitType, Integer> units, GamePlayerId gamePlayerId) {
        this.territory = territory;
        this.units = units;
        this.gamePlayerId = gamePlayerId;
    }

    public Territory getTerritory() {
        return territory;
    }

    public Map<UnitType, Integer> getUnits() {
        return units;
    }

    public GamePlayerId getGamePlayerId() {
        return gamePlayerId;
    }
}
