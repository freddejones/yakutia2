package se.freddejones.game.yakutia.model.dto;

public class AttackActionUpdate {

    private String territoryAttackSrc;
    private String territoryAttackDest;
    private int attackingNumberOfUnits;
    private Long gameId;
    private Long playerId;

    public AttackActionUpdate(String territoryAttackSrc, String territoryAttackDest, int attackingNumberOfUnits, Long gameId, Long playerId) {
        this.territoryAttackSrc = territoryAttackSrc;
        this.territoryAttackDest = territoryAttackDest;
        this.attackingNumberOfUnits = attackingNumberOfUnits;
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public String getTerritoryAttackSrc() {
        return territoryAttackSrc;
    }

    public void setTerritoryAttackSrc(String territoryAttackSrc) {
        this.territoryAttackSrc = territoryAttackSrc;
    }

    public String getTerritoryAttackDest() {
        return territoryAttackDest;
    }

    public void setTerritoryAttackDest(String territoryAttackDest) {
        this.territoryAttackDest = territoryAttackDest;
    }

    public int getAttackingNumberOfUnits() {
        return attackingNumberOfUnits;
    }

    public void setAttackingNumberOfUnits(int attackingNumberOfUnits) {
        this.attackingNumberOfUnits = attackingNumberOfUnits;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
