package se.freddejones.game.yakutia.model;

import se.freddejones.game.yakutia.entity.Unit;

public class BattleCalculator {

    public BattleCalculator() {
    }

    public BattleResult battle(Unit attackingUnit, Unit defendingUnit) {
        BattleResult battleResult = new BattleResult();
        battleResult.setDefendingTerritoryLosses(1);
        battleResult.setAttackingTerritoryLosses(0);
        battleResult.setTakenOver(false);
        if (defendingUnit.getStrength()-battleResult.getDefendingTerritoryLosses() <= 0) {
            battleResult.setTakenOver(true);
        }
        return battleResult;
    }
}
