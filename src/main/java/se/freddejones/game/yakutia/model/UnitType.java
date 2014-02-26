package se.freddejones.game.yakutia.model;

public enum UnitType {
    TANK ("TANK");

    private String unitType;

    private UnitType(String unitType) {
        this.unitType = unitType;
    }

    @Override
    public String toString() {
        return unitType;
    }
}
