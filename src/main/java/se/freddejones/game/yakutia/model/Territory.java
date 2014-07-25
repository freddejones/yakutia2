package se.freddejones.game.yakutia.model;


public enum Territory {
    NORWAY("NORWAY"),
    SWEDEN("SWEDEN"),
    FINLAND("FINLAND"),
    DENMARK("DENMARK"),
    ICELAND("ICELAND"),
    UKRAINA("UKRAINA"),
    TYSKLAND("TYSKLAND"),
    SKAUNE("SKAUNE"),
    TOMTEBODA("TOMTEBODA"),
    UNASSIGNED_TERRITORY("UNASSIGNED_TERRITORY");

    private String landArea;

    private Territory(String landArea) {
        this.landArea = landArea;
    }

    @Override
    public String toString() {
        return landArea;
    }

}
