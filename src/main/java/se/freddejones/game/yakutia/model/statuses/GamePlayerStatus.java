package se.freddejones.game.yakutia.model.statuses;

public enum GamePlayerStatus {

    INVITED ("INVITED"),
    ACCEPTED ("ACCEPTED"),
    DECLINED ("DECLINED"),
    ALIVE ("ALIVE"),
    DEAD ("DEAD");

    private String gamePlayerStatus;

    private GamePlayerStatus(String gamePlayerStatus) {
        this.gamePlayerStatus = gamePlayerStatus;
    }

    @Override
    public String toString() {
        return gamePlayerStatus;
    }

}
