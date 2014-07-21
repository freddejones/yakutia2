package se.freddejones.game.yakutia.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.BattleResult;
import se.freddejones.game.yakutia.model.UnitType;

import java.util.*;

@Component
public class BattleEngineCalculator {

    private final Dice dice;

    @Autowired
    public BattleEngineCalculator(Dice dice) {
        this.dice = dice;
    }

    public BattleResult battleForTerritory(Map<UnitType, Integer> attacking, Map<UnitType, Integer> defending) {

        Integer initialAttackingSoldierStrength = attacking.get(UnitType.SOLDIER);
        Integer attackingNumberDices = getAttackingNumberOfDices(initialAttackingSoldierStrength);

        Integer initialDefendingUnitStrength = defending.get(UnitType.SOLDIER);
        Integer defendingNumberDices = getDefendingNumberOfDices(initialDefendingUnitStrength, attackingNumberDices);

        List<Integer> sortedAttackingDiceRollResult = rollDices(attackingNumberDices);
        List<Integer> sortedDefendingDiceRollResult = rollDices(defendingNumberDices);

        Integer totalDefendingSoldierLosses = 0;
        for (Integer diceResultAttacking : sortedAttackingDiceRollResult) {
            for (Integer diceResultDefending : sortedDefendingDiceRollResult) {
                if (diceResultAttacking > diceResultDefending) {
                    totalDefendingSoldierLosses++;
                    sortedDefendingDiceRollResult.remove(diceResultDefending);
                    break;
                }
            }
        }

        Integer totalAttackingSoldierLosses = sortedDefendingDiceRollResult.size();

        int totalNumberOfUnitLossesForTerritory = initialDefendingUnitStrength - totalDefendingSoldierLosses;
        defending.put(UnitType.SOLDIER, totalNumberOfUnitLossesForTerritory);
        attacking.put(UnitType.SOLDIER, initialAttackingSoldierStrength - totalAttackingSoldierLosses);

        return new BattleResult(attacking, defending, totalNumberOfUnitLossesForTerritory == 0);
    }

    private Integer getDefendingNumberOfDices(Integer initialDefendingUnitStrength, Integer attackingNumberDices) {
        if (initialDefendingUnitStrength >= 2 && attackingNumberDices > 1) {
            return 2;
        } else {
            return 1;
        }
    }

    private Integer getAttackingNumberOfDices(Integer attackingSoldierStrength) {
        if (attackingSoldierStrength >= 3) {
            return 3;
        } else {
            return attackingSoldierStrength;
        }
    }

    private List<Integer> rollDices(int attackingNumberDices) {
        List<Integer> result = new ArrayList<>();
        for (int i=0; i<attackingNumberDices; i++) {
            result.add(dice.rollDice());
        }
        Collections.sort(result);
        Collections.reverse(result);
        return result;
    }
}
