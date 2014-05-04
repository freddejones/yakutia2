package se.freddejones.game.yakutia.entity;

import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.UnitType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UNITS")
@NamedQueries({
        @NamedQuery(name="Unit.getUnitsByLandArea",
                query = "SELECT u FROM Unit u where territory = :laName")
})
public class Unit implements Serializable {

	private int unitId;
    private int strength;
    private Territory territory;
    private UnitType typeOfUnit;
    private GamePlayer gamePlayer;

    @Id @GeneratedValue
    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int id) {
        this.unitId = id;
    }

    @Enumerated(EnumType.STRING)
    public Territory getTerritory() {
        return territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Enumerated(EnumType.STRING)
    public UnitType getTypeOfUnit() {
        return typeOfUnit;
    }

    public void setTypeOfUnit(UnitType typeOfUnit) {
        this.typeOfUnit = typeOfUnit;
    }

    @ManyToOne
    @JoinColumn(name = "gamePlayerId")
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public boolean isSameGameId(long gameId) {
        return this.gamePlayer.getGameId() == gameId;
    }
}
