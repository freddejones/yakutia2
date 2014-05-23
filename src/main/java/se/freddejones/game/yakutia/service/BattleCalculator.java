package se.freddejones.game.yakutia.service;

import org.springframework.stereotype.Component;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.BattleResult;

@Component
public class BattleCalculator {

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
