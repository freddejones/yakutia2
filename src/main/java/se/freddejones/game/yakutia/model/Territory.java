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



    public static Territory translateLandArea(String landArea) {
        landArea = landArea.toUpperCase();
        if (landArea.equals(NORWAY.toString())) {
            return NORWAY;
        } else if (landArea.equals(FINLAND.toString())) {
            return FINLAND;
        } else if (landArea.equals(SWEDEN.toString())) {
            return SWEDEN;
        } else if (landArea.equals(DENMARK.toString())) {
            return DENMARK;
        } else if (landArea.equals(ICELAND.toString())) {
            return ICELAND;
        } else if (landArea.equals(UKRAINA.toString())) {
            return UKRAINA;
        } else if (landArea.equals(TYSKLAND.toString())) {
            return TYSKLAND;
        } else if (landArea.equals(SKAUNE.toString())) {
            return SKAUNE;
        } else if (landArea.equals(TOMTEBODA.toString())) {
            return TOMTEBODA;
        } else if (landArea.equals(UNASSIGNED_TERRITORY.toString())) {
            return UNASSIGNED_TERRITORY;
        }

        return null;
    }
}
