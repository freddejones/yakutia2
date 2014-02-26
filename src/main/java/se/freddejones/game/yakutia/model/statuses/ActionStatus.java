package se.freddejones.game.yakutia.model.statuses;


public enum ActionStatus {

    PLACE_UNITS("PLACE_UNITS"),
    ATTACK("ATTACK"),
    MOVE("MOVE_UNITS");

    private String actionStatus;

    private ActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @Override
    public String toString() {
        return actionStatus;
    }

}
