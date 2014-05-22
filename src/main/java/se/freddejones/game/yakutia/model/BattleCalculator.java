package se.freddejones.game.yakutia.model;

import org.springframework.stereotype.Component;
import se.freddejones.game.yakutia.entity.Unit;

@Component
public class BattleCalculator {

    public BattleCalculator() {
    }

    public BattleResult battle(Unit attackingUnit, Unit defendingUnit) {
        BattleResult battleResult = new BattleResult();
        battleResult.setDefendingTerritoryLosses(1);
        battleResult.setAttackingTerritoryLosses(-1);
        battleResult.setTakenOver(false);
        if (defendingUnit.getStrength()-battleResult.getDefendingTerritoryLosses() <= 0) {
            battleResult.setTakenOver(true);
        }
        return battleResult;
    }
}
