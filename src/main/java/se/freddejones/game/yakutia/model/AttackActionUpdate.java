package se.freddejones.game.yakutia.model;

import java.util.Map;

public class AttackActionUpdate {

    private Territory territoryAttackSrc;
    private Territory territoryAttackDest;
    private Map<UnitType, Integer> attackingNumberOfUnits;
    private GamePlayerId gamePlayerId;
    private GamePlayerId defendingGamePlayerId;

    public Territory getTerritoryAttackSrc() {
        return territoryAttackSrc;
    }

    public void setTerritoryAttackSrc(Territory territoryAttackSrc) {
        this.territoryAttackSrc = territoryAttackSrc;
    }

    public Territory getTerritoryAttackDest() {
        return territoryAttackDest;
    }

    public void setTerritoryAttackDest(Territory territoryAttackDest) {
        this.territoryAttackDest = territoryAttackDest;
    }

    public Map<UnitType, Integer> getAttackingNumberOfUnits() {
        return attackingNumberOfUnits;
    }

    public void setAttackingNumberOfUnits(Map<UnitType, Integer> attackingNumberOfUnits) {
        this.attackingNumberOfUnits = attackingNumberOfUnits;
    }

    public GamePlayerId getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(GamePlayerId gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public GamePlayerId getDefendingGamePlayerId() {
        return defendingGamePlayerId;
    }

    public void setDefendingGamePlayerId(GamePlayerId defendingGamePlayerId) {
        this.defendingGamePlayerId = defendingGamePlayerId;
    }

    @Override
    public String toString() {
        return "AttackActionUpdate{" +
                "territoryAttackSrc=" + territoryAttackSrc +
                ", territoryAttackDest=" + territoryAttackDest +
                ", attackingNumberOfUnits=" + attackingNumberOfUnits +
                ", gamePlayerId=" + gamePlayerId +
                ", defendingGamePlayerId=" + defendingGamePlayerId +
                '}';
    }
}
