package se.freddejones.game.yakutia.model;

import se.freddejones.game.yakutia.entity.Unit;

public class BattleInformation {

    private Unit attackingUnit;
    private Unit defendingUnit;
    private Integer attackingStrength;

    public BattleInformation(Unit attackingUnit, Unit defendingUnit, Integer attackingStrength) {
        this.attackingUnit = attackingUnit;
        this.defendingUnit = defendingUnit;
        this.attackingStrength = attackingStrength;
    }

    public Unit getAttackingUnit() {
        return attackingUnit;
    }

    public Unit getDefendingUnit() {
        return defendingUnit;
    }

    public Integer getAttackingStrength() {
        return attackingStrength;
    }
}
