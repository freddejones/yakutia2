package se.freddejones.game.yakutia.model;

public class BattleResult {

    private final boolean isBattleWon;

    public BattleResult(boolean isBattleWon) {
        this.isBattleWon = isBattleWon;
    }

    public boolean isBattleWon() {
        return isBattleWon;
    }
}
