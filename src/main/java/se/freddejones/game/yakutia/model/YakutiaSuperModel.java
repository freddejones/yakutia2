package se.freddejones.game.yakutia.model;

import java.io.Serializable;
import java.util.List;

/**
 * User: Fredde
 * Date: 11/24/13 10:20 PM
 */
public class YakutiaSuperModel implements Serializable {

    List<TerritoryDTO> landAreas;

    public List<TerritoryDTO> getLandAreas() {
        return landAreas;
    }

    public void setLandAreas(List<TerritoryDTO> landAreas) {
        this.landAreas = landAreas;
    }
}
