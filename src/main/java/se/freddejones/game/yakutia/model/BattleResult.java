package se.freddejones.game.yakutia.model;

import java.util.Map;

public class BattleResult {

    private final Map<UnitType, Integer> attackingUnitsLeft;
    private final Map<UnitType, Integer> defendingUnitsLeft;
    private final boolean isTakenOver;

    public BattleResult(Map<UnitType, Integer> attackingUnitsLeft, Map<UnitType, Integer> defendingUnitsLeft, boolean isTakenOver) {
        this.attackingUnitsLeft = attackingUnitsLeft;
        this.defendingUnitsLeft = defendingUnitsLeft;
        this.isTakenOver = isTakenOver;
    }

    public Map<UnitType, Integer> getAttackingUnitsLeft() {
        return attackingUnitsLeft;
    }

    public Map<UnitType, Integer> getDefendingUnitsLeft() {
        return defendingUnitsLeft;
    }

    public boolean isTakenOver() {
        return isTakenOver;
    }
}
