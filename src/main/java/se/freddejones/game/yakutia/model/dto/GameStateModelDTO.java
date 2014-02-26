package se.freddejones.game.yakutia.model.dto;

public class GameStateModelDTO {

    private String state;
    private Long playerId;
    private Long gameId;
//    private PlaceUnitUpdate placeUnitUpdate;
//    private AttackActionUpdate attackActionUpdate;
//    private MoveUnitUpdate moveUnitUpdate;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

//    public PlaceUnitUpdate getPlaceUnitUpdate() {
//        return placeUnitUpdate;
//    }
//
//    public void setPlaceUnitUpdate(PlaceUnitUpdate placeUnitUpdate) {
//        this.placeUnitUpdate = placeUnitUpdate;
//    }
//
//    public AttackActionUpdate getAttackActionUpdate() {
//        return attackActionUpdate;
//    }
//
//    public void setAttackActionUpdate(AttackActionUpdate attackActionUpdate) {
//        this.attackActionUpdate = attackActionUpdate;
//    }
//
//    public MoveUnitUpdate getMoveUnitUpdate() {
//        return moveUnitUpdate;
//    }
//
//    public void setMoveUnitUpdate(MoveUnitUpdate moveUnitUpdate) {
//        this.moveUnitUpdate = moveUnitUpdate;
//    }
}
