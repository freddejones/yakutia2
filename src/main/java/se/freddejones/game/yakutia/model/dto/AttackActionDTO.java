package se.freddejones.game.yakutia.model.dto;

import java.util.Map;

public class AttackActionDTO {

    private String territoryAttackSrc;
    private String territoryAttackDst;
    private Map<String, Integer> attackingUnits;
    private Long attackingGamePlayerId;
    private Long defendingGamePlayerId;

    public Long getDefendingGamePlayerId() {
        return defendingGamePlayerId;
    }

    public void setDefendingGamePlayerId(Long defendingGamePlayerId) {
        this.defendingGamePlayerId = defendingGamePlayerId;
    }

    public Long getAttackingGamePlayerId() {
        return attackingGamePlayerId;
    }

    public void setAttackingGamePlayerId(Long attackingGamePlayerId) {
        this.attackingGamePlayerId = attackingGamePlayerId;
    }

    public Map<String, Integer> getAttackingUnits() {
        return attackingUnits;
    }

    public void setAttackingUnits(Map<String, Integer> attackingUnits) {
        this.attackingUnits = attackingUnits;
    }

    public String getTerritoryAttackDst() {
        return territoryAttackDst;
    }

    public void setTerritoryAttackDst(String territoryAttackDst) {
        this.territoryAttackDst = territoryAttackDst;
    }

    public String getTerritoryAttackSrc() {
        return territoryAttackSrc;
    }

    public void setTerritoryAttackSrc(String territoryAttackSrc) {
        this.territoryAttackSrc = territoryAttackSrc;
    }
}
