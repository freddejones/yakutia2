package se.freddejones.game.yakutia.model;

public class UnitId {

    private final Integer unitId;

    public UnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    @Override
    public String toString() {
        return "UnitId{" +
                "unitId=" + unitId +
                '}';
    }
}
