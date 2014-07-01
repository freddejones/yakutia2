package se.freddejones.game.yakutia.model;

import se.freddejones.game.yakutia.model.GamePlayerId;

public class AttackActionUpdate {

    private Long territoryAttackSrc;
    private Long territoryAttackDest;
    private Long attackingNumberOfUnits;
    private GamePlayerId gamePlayerId;

    public Long getTerritoryAttackSrc() {
        return territoryAttackSrc;
    }

    public void setTerritoryAttackSrc(Long territoryAttackSrc) {
        this.territoryAttackSrc = territoryAttackSrc;
    }

    public Long getTerritoryAttackDest() {
        return territoryAttackDest;
    }

    public void setTerritoryAttackDest(Long territoryAttackDest) {
        this.territoryAttackDest = territoryAttackDest;
    }

    public Long getAttackingNumberOfUnits() {
        return attackingNumberOfUnits;
    }

    public void setAttackingNumberOfUnits(Long attackingNumberOfUnits) {
        this.attackingNumberOfUnits = attackingNumberOfUnits;
    }

    public GamePlayerId getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(GamePlayerId gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    @Override
    public String toString() {
        return "AttackActionUpdateDTO{" +
                "territoryAttackSrc=" + territoryAttackSrc +
                ", territoryAttackDest=" + territoryAttackDest +
                ", attackingNumberOfUnits=" + attackingNumberOfUnits +
                ", gamePlayerId=" + gamePlayerId +
                '}';
    }
}
