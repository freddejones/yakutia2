package se.freddejones.game.yakutia.model;

public class BattleResult {

    private boolean isTakenOver;
    private int defendingTerritoryLosses;
    private int attackingTerritoryLosses;

    public boolean isTakenOver() {
        return isTakenOver;
    }

    public void setTakenOver(boolean isTakenOver) {
        this.isTakenOver = isTakenOver;
    }

    public int getDefendingTerritoryLosses() {
        return defendingTerritoryLosses;
    }

    public void setDefendingTerritoryLosses(int defendingTerritoryLosses) {
        this.defendingTerritoryLosses = defendingTerritoryLosses;
    }

    public int getAttackingTerritoryLosses() {
        return attackingTerritoryLosses;
    }

    public void setAttackingTerritoryLosses(int attackingTerritoryLosses) {
        this.attackingTerritoryLosses = attackingTerritoryLosses;
    }
}
