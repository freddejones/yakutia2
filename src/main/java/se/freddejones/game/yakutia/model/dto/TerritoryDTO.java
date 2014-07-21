package se.freddejones.game.yakutia.model.dto;

import java.io.Serializable;

public class TerritoryDTO implements Serializable {

    private String landName;
    private Long units;
    private Long territoryId;
    private boolean isOwnedByPlayer;

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public Long getUnits() {
        return units;
    }

    public void setUnits(Long units) {
        this.units = units;
    }

    public Long getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Long territoryId) {
        this.territoryId = territoryId;
    }

    public boolean isOwnedByPlayer() {
        return isOwnedByPlayer;
    }

    public void setOwnedByPlayer(boolean isOwnedByPlayer) {
        this.isOwnedByPlayer = isOwnedByPlayer;
    }
}
