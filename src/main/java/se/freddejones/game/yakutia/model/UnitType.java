package se.freddejones.game.yakutia.model;

public enum UnitType {
    AIRPLANE ("AIRPLANE"),
    TANK ("TANK"),
    SOLDIER ("SOLDIER");

    private String unitType;

    private UnitType(String unitType) {
        this.unitType = unitType;
    }

    @Override
    public String toString() {
        return unitType;
    }
}
